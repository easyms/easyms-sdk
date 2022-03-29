/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.easyms.security.azuread.ms.config;

import com.azure.spring.aad.webapi.AADJwtBearerTokenAuthenticationConverter;
import com.azure.spring.aad.webapi.AADResourceServerProperties;
import com.azure.spring.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter;
import com.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DefaultWebSecurityConfig extends AADResourceServerWebSecurityConfigurerAdapter {


    @Autowired
    AADResourceServerProperties properties;

    @Autowired
    RawAADAppRolesConverter rawAADAppRolesConverter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(
                        new CustomAADJwtBearerTokenAuthenticationConverter(
                                properties.getPrincipalClaimName(), properties.getClaimToAuthorityPrefixMap(), rawAADAppRolesConverter));
        // @formatter:off

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .authorizeRequests().anyRequest().authenticated();

    }
}
