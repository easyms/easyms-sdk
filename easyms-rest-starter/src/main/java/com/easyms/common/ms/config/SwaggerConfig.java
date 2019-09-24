package com.easyms.common.ms.config;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.ZonedDateTime;
import java.util.Collections;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.or;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author abessa
 */
@Configuration
@EnableSwagger2
@AllArgsConstructor
public class SwaggerConfig {

    private final SwaggerProperties properties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select().paths(s -> Predicates.containsPattern(properties.getPaths()).apply(s))
                .build().directModelSubstitute(ZonedDateTime.class, String.class);
    }


    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getApiInfoTitle())
                .description(properties.getApiInfoDescription())
                .version(properties.getApiInfoVersion())
                .license(properties.getApiInfoLicense())
                .licenseUrl(properties.getApiInfoLicenseUrl())
                .contact(new Contact(properties.getApiInfoContactName(), properties.getApiInfoContactURL(), properties.getApiInfoContactEmail()))
                .build();
    }
}