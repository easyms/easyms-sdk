package com.easyms.sampleapp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.inject.Inject;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
@ComponentScan(basePackages = "com.easyms.azure.test")
public class JwtBearerTokenGeneratorTest {


    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Inject
    protected MockMvc mockMvc;


    private static final char[] PASSWORD = "Easyms2020".toCharArray();
    private static final String ALIAS = "jwt";

    private static final String API_HR_ID = "/api/v1/clients/{id}";

    @Inject
    ObjectMapper objectMapper;


    @Test
    void should_return_token_with_jwt_bearer_credentials() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1").with(bearerTokenInternal())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String jwtToken = createToken();
        System.out.println("Token =" + jwtToken);

    }

    protected RequestPostProcessor bearerTokenInternal() {
        return mockHttpServletRequest -> {
            try {
                mockHttpServletRequest.addHeader(AUTHORIZATION_HEADER, "%s %s".formatted(BEARER_TOKEN_TYPE, createToken()));
            } catch (JOSEException e) {
                throw new RuntimeException("Unable to create token", e);
            } catch (ParseException e) {
                throw new RuntimeException("Unable to create token", e);
            }
            return mockHttpServletRequest;
        };
    }

    public String createToken() throws JOSEException, ParseException {

        KeyPair keyPair = getkeyPair();

        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(pub)
                .privateKey(priv)
                .keyUse(KeyUse.SIGNATURE)
                .keyID("myKeyAnis.net")
                .build();

        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());

        final JSONArray roles = new JSONArray().appendElement("SUPER_ADMIN_CLIENT");

        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("test-jwt-bearer-client")
                .issuer("https://c2id.com")
                .claim("roles", roles)
                .claim("upn", "abessa@abessatest.onmicrosoft.com")
                .expirationTime(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                claimsSet);

        // Compute the RSA signature
        signedJWT.sign(signer);

        String tokenAsString = signedJWT.serialize();

        // On the consumer side, parse the JWS and verify its RSA signature
        signedJWT = SignedJWT.parse(tokenAsString);

        JWSVerifier verifier = new RSASSAVerifier(rsaKey.toPublicJWK());
        assertTrue(signedJWT.verify(verifier));

        // Retrieve / verify the JWT claims according to the app requirements
        assertEquals("test-jwt-bearer-client", signedJWT.getJWTClaimsSet().getSubject());
        assertEquals("https://c2id.com", signedJWT.getJWTClaimsSet().getIssuer());
        //assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
        return tokenAsString;
    }

    public KeyPair getkeyPair() {
        String jwtBearerKeystore = "classpath:jwtbearer-test.jks";
        try {
            File secretsFolder = ResourceUtils.getFile(jwtBearerKeystore);
            InputStream inputStream = new FileInputStream(secretsFolder);
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new InputStreamResource(inputStream), PASSWORD);
            return keyStoreKeyFactory.getKeyPair(ALIAS);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Cannot found private key from keystore path " + jwtBearerKeystore, e);
        }
    }

}
