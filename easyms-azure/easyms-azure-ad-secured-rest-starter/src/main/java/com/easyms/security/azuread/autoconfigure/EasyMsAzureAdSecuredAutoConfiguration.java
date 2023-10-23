package com.easyms.security.azuread.autoconfigure;


import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.AadAutoConfiguration;
import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.azuread.ms.config.InternalTokenProperties;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import com.easyms.security.azuread.ms.filter.RoleAndAuthoritiesMappingProperties;
import com.easyms.security.azuread.ms.filter.RolesConverter;
import com.easyms.security.common.ms.config.feign.AuthenticationToTokenMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Objects;

/**
 * @author khames.
 */
@Data
@Slf4j
@ConditionalOnProperty(value = "easyms.secured.azuread.enabled", havingValue = "true")
@EnableConfigurationProperties({RoleAndAuthoritiesMappingProperties.class, InternalTokenProperties.class})
@ComponentScan("com.easyms.security.azuread.ms")
@AutoConfigureBefore({EasyMsAutoConfiguration.class, AadAutoConfiguration.class})
@AutoConfiguration
@OpenAPIDefinition
public class EasyMsAzureAdSecuredAutoConfiguration {

    private final InternalTokenProperties internalTokenProperties;
    private final RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties;

    @Bean
    @ConditionalOnMissingBean(RolesConverter.class)
    public RawAADAppRolesConverter rawAADAppRolesConverter() {
        return new RawAADAppRolesConverter(roleAndAuthoritiesMappingProperties);
    }

    @Bean
    public AuthenticationToTokenMapper authenticationToTokenMapper() {
        return (AuthenticationToTokenMapper) auth -> {
            Jwt token = ((JwtAuthenticationToken) auth).getToken();
            return token.getTokenValue();
        };
    }


}

