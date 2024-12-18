package com.easyms.security.azuread.ms.config;

import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.properties.AadAuthenticationProperties;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.jwt.AadJwtIssuerValidator;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.properties.AadAuthorizationServerEndpoints;
import com.azure.spring.cloud.autoconfigure.implementation.aad.utils.AadRestTemplateCreator;
import com.nimbusds.jwt.SignedJWT;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
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
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Configuration
public class CustomJwtDecoderConfiguration {

    @Autowired
    private InternalTokenProperties internalTokenProperties;
    private final RestTemplateBuilder restTemplateBuilder;

    private final AadAuthenticationProperties aadAuthenticationProperties;

    public CustomJwtDecoderConfiguration(RestTemplateBuilder restTemplateBuilder, AadAuthenticationProperties aadAuthenticationProperties) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.aadAuthenticationProperties = aadAuthenticationProperties;
    }

    @Bean
    @Primary
    public JwtDecoder customJwtDecoder() {
        var standardDecoder = standardJwtDecoder();
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

    JwtDecoder standardJwtDecoder() {
        AadAuthorizationServerEndpoints identityEndpoints = new AadAuthorizationServerEndpoints(aadAuthenticationProperties.getProfile().getEnvironment().getActiveDirectoryEndpoint(), aadAuthenticationProperties.getProfile().getTenantId());
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withJwkSetUri(identityEndpoints.getJwkSetEndpoint()).restOperations(AadRestTemplateCreator.createRestTemplate(this.restTemplateBuilder)).build();
        List<OAuth2TokenValidator<Jwt>> validators = this.createDefaultValidator(aadAuthenticationProperties);
        nimbusJwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator(validators));
        return nimbusJwtDecoder;
    }

    List<OAuth2TokenValidator<Jwt>> createDefaultValidator(AadAuthenticationProperties aadAuthenticationProperties) {
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList();
        List<String> validAudiences = new ArrayList();
        if (StringUtils.hasText(aadAuthenticationProperties.getAppIdUri())) {
            validAudiences.add(aadAuthenticationProperties.getAppIdUri());
        }

        if (StringUtils.hasText(aadAuthenticationProperties.getCredential().getClientId())) {
            validAudiences.add(aadAuthenticationProperties.getCredential().getClientId());
        }

        if (!validAudiences.isEmpty()) {
            Objects.requireNonNull(validAudiences);
            validators.add(new JwtClaimValidator("aud", a -> validAudiences.containsAll((Collection<?>) a)));
        }

        validators.add(new AadJwtIssuerValidator());
        validators.add(new JwtTimestampValidator());
        return validators;
    }

}
