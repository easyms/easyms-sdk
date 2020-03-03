package com.easyms.security.oauth2.ms.config.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author khames.
 */
@Configuration
@ConditionalOnMissingBean(RoutesHandler.class)
public class DefaultRoutesHandlerConfig {

    @Bean
    public RoutesHandler routesHandler() {
        return new RoutesHandler() {
        };
    }
}
