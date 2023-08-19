package com.easyms.security.azuread.ms.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@Order(1)
public class ActuatorSecurityConfig {
    /*@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityMatchers(securityMatcher -> securityMatcher
                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                        .requestMatchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)))
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().hasRole("ACTUATOR").anyRequest().permitAll()
                ).httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint()));
        return http.build();
    }


    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("actuator");
        return basicAuthenticationEntryPoint;
    }
*/
}

