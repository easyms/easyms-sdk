package com.easyms.security.oauth2.autoconfigure;


import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author khames.
 */
@AutoConfiguration
@Data
@Slf4j
@ComponentScan(basePackages = {"com.easyms.security.oauth2.ms"})
@ConditionalOnProperty(value = "easyms.secured.oauth2", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
@OpenAPIDefinition
public class EasyMsSecuredAutoConfiguration {

}
