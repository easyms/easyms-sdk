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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;



public class DefaultWebSecurityConfig {
/*
    @Autowired
    AadResourceServerProperties properties;

    @Autowired
    RawAADAppRolesConverter rawAADAppRolesConverter;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AadResourceServerHttpSecurityConfigurer aadResourceServerHttpSecurityConfigurer = new AadResourceServerHttpSecurityConfigurer().jwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter(rawAADAppRolesConverter, properties.getClaimToAuthorityPrefixMap()));
        http.apply(aadResourceServerHttpSecurityConfigurer)
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated();
        return http.build();

    }*/
}
