package com.easyms.security.azuread.ms;


import com.microsoft.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter;
import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationProperties;
import com.microsoft.azure.spring.autoconfigure.aad.ServiceEndpointsProperties;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import com.nimbusds.jose.util.ResourceRetriever;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationFilterAutoConfiguration.PROPERTY_PREFIX;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(value = "easyms.secured.azuread.enabled", havingValue = "true")
@EnableConfigurationProperties(RoleAndAuthoritiesMappingProperties.class)
@ComponentScan
public class EasyMsAzureAdSecuredAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AADAppRoleStatelessAuthenticationFilter.class)
    @ConditionalOnProperty(prefix = PROPERTY_PREFIX, value = "session-stateless", havingValue = "true")
    public WithAuthoritiesAADAppRoleStatelessAuthenticationFilter customAzureADStatelessAuthFilter(ResourceRetriever resourceRetriever,
                                     ServiceEndpointsProperties serviceEndpointsProps,
                                     AADAuthenticationProperties aadAuthProps,
                                     RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
        log.info("Creating WithAuthoritiesAADAppRoleStatelessAuthenticationFilter bean.");
        final boolean useExplicitAudienceCheck = true;
        return new WithAuthoritiesAADAppRoleStatelessAuthenticationFilter(new UserPrincipalManager(serviceEndpointsProps, aadAuthProps,
                resourceRetriever, useExplicitAudienceCheck), roleAndAuthoritiesMappingProperties);
    }
}
