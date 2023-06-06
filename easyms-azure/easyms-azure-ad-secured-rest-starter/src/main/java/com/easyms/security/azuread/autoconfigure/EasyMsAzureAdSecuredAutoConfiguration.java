package com.easyms.security.azuread.autoconfigure;


import com.azure.spring.aad.webapi.AADResourceServerConfiguration;
import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.azuread.ms.config.InternalTokenProperties;
import com.easyms.security.azuread.ms.filter.RoleAndAuthoritiesMappingProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author khames.
 */
@Data
@Slf4j
@ConditionalOnProperty(value = "easyms.secured.azuread.enabled", havingValue = "true")
@EnableConfigurationProperties({RoleAndAuthoritiesMappingProperties.class, InternalTokenProperties.class})
@ComponentScan("com.easyms.security.azuread.ms")
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
@AllArgsConstructor
@AutoConfiguration
@OpenAPIDefinition
public class EasyMsAzureAdSecuredAutoConfiguration {

    private final InternalTokenProperties internalTokenProperties;
    private final AADResourceServerConfiguration aadResourceServerConfiguration;

}

