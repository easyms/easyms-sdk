package com.easyms.security.azuread.autoconfigure;


import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.azuread.ms.config.EasymsAdUserPrincipalManager;
import com.easyms.security.azuread.ms.config.InternalTokenProperties;
import com.easyms.security.azuread.ms.filter.RoleAndAuthoritiesMappingProperties;
import com.easyms.security.azuread.ms.filter.WithAuthoritiesAADAppRoleStatelessAuthenticationFilter;
import com.microsoft.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter;
import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationProperties;
import com.microsoft.azure.spring.autoconfigure.aad.ServiceEndpointsProperties;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import com.nimbusds.jose.util.ResourceRetriever;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationFilterAutoConfiguration.PROPERTY_PREFIX;

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

    @Bean
    @ConditionalOnMissingBean(AADAppRoleStatelessAuthenticationFilter.class)
    @ConditionalOnProperty(prefix = PROPERTY_PREFIX, value = "session-stateless", havingValue = "true")
    public WithAuthoritiesAADAppRoleStatelessAuthenticationFilter customAzureADStatelessAuthFilter(UserPrincipalManager userPrincipalManager,
                                                                                                   RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
        log.info("Creating WithAuthoritiesAADAppRoleStatelessAuthenticationFilter bean.");
        final boolean useExplicitAudienceCheck = true;
        return new WithAuthoritiesAADAppRoleStatelessAuthenticationFilter(userPrincipalManager, roleAndAuthoritiesMappingProperties);
    }

    @Bean
    @ConditionalOnMissingBean(UserPrincipalManager.class)
    @ConditionalOnProperty(prefix = PROPERTY_PREFIX, value = "session-stateless", havingValue = "true")
    public UserPrincipalManager provideUserPrincipalManager(ResourceRetriever resourceRetriever,
                                                            ServiceEndpointsProperties serviceEndpointsProps,
                                                            AADAuthenticationProperties aadAuthProps) {
        log.info("Creating UserPrincipalManager bean.");
        final boolean useExplicitAudienceCheck = true;
        return new EasymsAdUserPrincipalManager(serviceEndpointsProps, aadAuthProps,
                resourceRetriever, useExplicitAudienceCheck, internalTokenProperties);
    }
}
