/*
package com.easyms.security.azuread.ms.config;

import com.azure.spring.aad.webapi.AADResourceServerConfiguration;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.DelegatingOAuth3TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class CustomJwtDecoderConfiguration {

    private final AADResourceServerConfiguration aadResourceServerConfiguration;
    private final InternalTokenProperties internalTokenProperties;

    @Bean
    @Primary
    public JwtDecoder customJwtDecoder() {
        var standardDecoder = aadResourceServerConfiguration.jwtDecoder();
        var internalDecoder = internalJwtDecoder();

        return token -> {

            if (internalTokenProperties.isEnabled()) {
                try {
                    SignedJWT signedJWT = SignedJWT.parse(token);
                    if (signedJWT.getJWTClaimsSet().getIssuer().equals(internalTokenProperties.getIssuer())) {
                        return internalDecoder.decode(token);
                    } else {
                        return standardDecoder.decode(token);
                    }
                } catch (ParseException e) {
                    throw new JwtException("Exception while parsing JWT", e);
                }
            }

            return standardDecoder.decode(token);
        };
    }


    public JwtDecoder internalJwtDecoder() {
        var nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey(getInternalPubKey()).build();
        List<OAuth2TokenValidator<Jwt>> internalJwtValidators = createInternalJwtValidators();
        nimbusJwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(internalJwtValidators));

        return nimbusJwtDecoder;
    }

    private List<OAuth2TokenValidator<Jwt>> createInternalJwtValidators() {
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();

        validators.add(new JwtIssuerValidator(internalTokenProperties.getIssuer()));
        validators.add(new JwtTimestampValidator());
        return validators;
    }


    private RSAPublicKey getInternalPubKey() {

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            Reader pubKeyReader;
            if (StringUtils.hasText(internalTokenProperties.getPubKeyPath())) {
                File secretsFolder = ResourceUtils.getFile(internalTokenProperties.getPubKeyPath());
                pubKeyReader = new InputStreamReader(new FileInputStream(secretsFolder));
            } else if (StringUtils.hasText(internalTokenProperties.getPubKeyValue())) {
                pubKeyReader = new StringReader(internalTokenProperties.getPubKeyValue());
            } else {
                throw new IllegalStateException("Either pubKey path or pubKey value should be provided");
            }


            byte[] publicKeyBytes = new PemReader(pubKeyReader).readPemObject().getContent();
            return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException("Unable to load Internal PubKey", e);
        }

    }
}
*/
