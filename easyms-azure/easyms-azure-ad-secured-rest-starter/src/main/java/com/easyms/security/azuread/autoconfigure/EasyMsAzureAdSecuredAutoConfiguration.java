package com.easyms.security.azuread.autoconfigure;


import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.AadAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.properties.AadResourceServerProperties;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.azuread.ms.config.CustomJwtGrantedAuthoritiesConverter;
import com.easyms.security.azuread.ms.config.InternalTokenProperties;
import com.easyms.security.azuread.ms.config.JwtDecoderInterceptor;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import com.easyms.security.azuread.ms.filter.RoleAndAuthoritiesMappingProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author khames.
 */
@Data
@Slf4j
@ConditionalOnProperty(value = "easyms.secured.azuread.enabled", havingValue = "true")
@EnableConfigurationProperties({RoleAndAuthoritiesMappingProperties.class, InternalTokenProperties.class})
@ComponentScan("com.easyms.security.azuread.ms")
@AutoConfigureBefore({EasyMsAutoConfiguration.class, AadAutoConfiguration.class})
@AllArgsConstructor
@AutoConfiguration
@OpenAPIDefinition
public class EasyMsAzureAdSecuredAutoConfiguration {

    private final InternalTokenProperties internalTokenProperties;

}

