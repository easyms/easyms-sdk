/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.easyms.security.azuread.ms.config;

import com.azure.spring.aad.webapi.AADResourceServerProperties;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class DefaultWebSecurityConfig {


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AADResourceServerProperties properties, RawAADAppRolesConverter rawAADAppRolesConverter) throws Exception {
        // @formatter:off
        http.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new CustomAADJwtBearerTokenAuthenticationConverter(
                                properties.getPrincipalClaimName(), properties.getClaimToAuthorityPrefixMap(), rawAADAppRolesConverter))));
        // @formatter:off

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .anyRequest()
                        .authenticated());
        return http.build();

    }
}

