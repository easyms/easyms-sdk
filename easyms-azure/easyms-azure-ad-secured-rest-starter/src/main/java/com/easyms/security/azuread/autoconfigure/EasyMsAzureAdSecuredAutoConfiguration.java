package com.easyms.security.azuread.autoconfigure;


import com.azure.spring.aad.AADAuthorizationServerEndpoints;
import com.azure.spring.aad.webapi.AADResourceServerConfiguration;
import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.azuread.ms.config.InternalTokenProperties;
import com.easyms.security.azuread.ms.filter.RoleAndAuthoritiesMappingProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.List;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(value = "easyms.secured.azuread.enabled", havingValue = "true")
@EnableConfigurationProperties({RoleAndAuthoritiesMappingProperties.class, InternalTokenProperties.class})
@ComponentScan("com.easyms.security.azuread.ms")
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
@AllArgsConstructor
public class EasyMsAzureAdSecuredAutoConfiguration {

    private final InternalTokenProperties internalTokenProperties;
    private final AADResourceServerConfiguration aadResourceServerConfiguration;



//    @Bean
//    @ConditionalOnMissingBean(AADAppRoleStatelessAuthenticationFilter.class)
//    @ConditionalOnProperty(prefix = PROPERTY_PREFIX, value = "session-stateless", havingValue = "true")
//    public WithAuthoritiesAADAppRoleStatelessAuthenticationFilter customAzureADStatelessAuthFilter(UserPrincipalManager userPrincipalManager,
//                                                                                                   RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
//        log.info("Creating WithAuthoritiesAADAppRoleStatelessAuthenticationFilter bean.");
//        final boolean useExplicitAudienceCheck = true;
//        return new WithAuthoritiesAADAppRoleStatelessAuthenticationFilter(userPrincipalManager, roleAndAuthoritiesMappingProperties);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(UserPrincipalManager.class)
//    @ConditionalOnProperty(prefix = PROPERTY_PREFIX, value = "session-stateless", havingValue = "true")
//    public UserPrincipalManager provideUserPrincipalManager(ResourceRetriever resourceRetriever,
//                                                            AADAuthorizationServerEndpoints aadAuthorizationServerEndpoints,
//                                                            AADAuthenticationProperties aadAuthProps) {
//        log.info("Creating UserPrincipalManager bean.");
//        final boolean useExplicitAudienceCheck = true;
//        return new EasymsAdUserPrincipalManager(aadAuthorizationServerEndpoints, aadAuthProps,
//                resourceRetriever, useExplicitAudienceCheck, internalTokenProperties);
//    }
}
