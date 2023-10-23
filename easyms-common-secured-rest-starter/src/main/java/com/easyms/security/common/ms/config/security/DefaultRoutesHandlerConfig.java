package com.easyms.security.common.ms.config.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mob.
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
