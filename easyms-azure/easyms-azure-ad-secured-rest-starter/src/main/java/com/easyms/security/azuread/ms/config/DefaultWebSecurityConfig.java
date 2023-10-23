/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.easyms.security.azuread.ms.config;

import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.properties.AadResourceServerProperties;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class DefaultWebSecurityConfig {

    private final AadResourceServerProperties properties;

    private final RawAADAppRolesConverter rawAADAppRolesConverter;

    @Bean
    SecurityFilterChain filterChainDefault(HttpSecurity http) throws Exception {
        AadResourceServerHttpSecurityConfigurer aadResourceServerHttpSecurityConfigurer = new AadResourceServerHttpSecurityConfigurer()
                .jwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter(rawAADAppRolesConverter, properties.getClaimToAuthorityPrefixMap()));

        // @formatter:off
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);

        // @formatter:off
        http.apply(aadResourceServerHttpSecurityConfigurer)
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated();

        return http.build();
    }
}