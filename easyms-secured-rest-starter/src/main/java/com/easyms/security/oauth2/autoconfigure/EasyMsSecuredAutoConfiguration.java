package com.easyms.security.oauth2.autoconfigure;


import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.common.ms.config.feign.AuthenticationToTokenMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;

/**
 * @author khames.
 */
@Data
@Slf4j
@AutoConfiguration
@ComponentScan(basePackages = {"com.easyms.security.oauth2.ms"})
@ConditionalOnProperty(value="easyms.secured.oauth2", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
public class EasyMsSecuredAutoConfiguration {

    @Bean
    public AuthenticationToTokenMapper authenticationToTokenMapper() {
        return (AuthenticationToTokenMapper) auth -> {
            var details = (OAuth2AuthorizationCodeAuthenticationToken) auth.getDetails();
            return details.getAccessToken().getTokenValue();
        };
    }

}
