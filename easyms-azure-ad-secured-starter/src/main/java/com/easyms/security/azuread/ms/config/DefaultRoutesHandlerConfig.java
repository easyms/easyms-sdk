package com.easyms.security.azuread.ms.config;

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
