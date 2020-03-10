package com.easyms.security.oauth2.autoconfigure;


import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.rest.ms.config.SwaggerProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.ZonedDateTime;
import java.util.Collections;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.or;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.easyms.security.oauth2.ms"})
@ConditionalOnProperty(value="easyms.secured.oauth2", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
public class EasyMsSecuredAutoConfiguration {

}
