package com.easyms.security.common.ms.config.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

/**
 * @author mob.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class PublicEndpointsWebSecurityConfig {

    private final CORSFilter corsFilter;
    private final RoutesHandler routesHandler;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain filterChainPublic(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .securityMatchers(securityMatcher -> securityMatcher
                        .requestMatchers(routesHandler.technicalEndPoints())
                        .requestMatchers(routesHandler.publicEndpoints()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                       .authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .httpBasic(HttpBasicConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class);

        // @formatter:on

        return http.build();
    }
}
