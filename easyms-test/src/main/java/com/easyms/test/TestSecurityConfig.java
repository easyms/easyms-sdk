package com.easyms.test;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class TestSecurityConfig {
    /**
     * This decoder allows you to run tests without needing a Keycloak running.
     * <p>
     * We could use @MockUser of the spring-security-test package but with this decode,
     * we will cover the code inside the KeycloakSecurityConfig class.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return TestJWTUtils::decode;
    }
}
