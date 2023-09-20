package com.easyms.security.azuread.ms.config;

import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.properties.AadResourceServerProperties;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import com.easyms.security.azuread.ms.filter.CORSFilter;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * @author khames.
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@AllArgsConstructor
public class PublicEndpointsWebSecurityConfig {

    private final CORSFilter corsFilter;
    private final RoutesHandler routesHandler;

    @Autowired
    AadResourceServerProperties properties;

    @Autowired
    RawAADAppRolesConverter rawAADAppRolesConverter;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(routesHandler.technicalEndPoints())
                .requestMatchers(routesHandler.publicEndpoints());
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("actuator");
        return basicAuthenticationEntryPoint;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        // Public Endpoints Web Security Config
        http
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class);

        //Actuator Security Config
        /*http.securityMatchers(securityMatcher -> securityMatcher
                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                        .requestMatchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)))
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().hasRole("ACTUATOR").anyRequest().permitAll()
                ).httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint()));*/

        // Aad Security Config
        AadResourceServerHttpSecurityConfigurer aadResourceServerHttpSecurityConfigurer = new AadResourceServerHttpSecurityConfigurer().jwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter(rawAADAppRolesConverter, properties.getClaimToAuthorityPrefixMap()));
        http.apply(aadResourceServerHttpSecurityConfigurer)
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated();
        return http.build();

        // @formatter:on
    }
}
