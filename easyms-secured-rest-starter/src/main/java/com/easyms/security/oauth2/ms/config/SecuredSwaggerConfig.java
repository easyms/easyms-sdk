package com.easyms.security.oauth2.ms.config;


import com.easyms.rest.ms.config.SwaggerProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
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
public class SecuredSwaggerConfig {
    private static final String SECURITY_SCHEMA_O_AUTH_2 = "oauth2schema";


    private final SwaggerProperties properties;


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

    @Bean
    public Docket securedSwaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select().paths(or(containsPattern(properties.getPaths())))
                .build()
                .directModelSubstitute(ZonedDateTime.class, String.class)
                .securityContexts(singletonList(securityContext()))
                .securitySchemes(singletonList(securitySchema()));
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(singletonList(securityReference()))
                .forPaths(regex(properties.getPaths()))
                .build();
    }

    private SecurityScheme securitySchema() {
        AuthorizationScope scope = new AuthorizationScope(properties.getScopes(), properties.getScopes());
        ClientCredentialsGrant grantType = new ClientCredentialsGrant(properties.getTokenUrl());
        return new OAuth(SECURITY_SCHEMA_O_AUTH_2, Collections.singletonList(scope), singletonList(grantType));
    }

    private SecurityReference securityReference() {
        return SecurityReference.builder()
                .reference(SECURITY_SCHEMA_O_AUTH_2)
                .scopes(singletonList(new AuthorizationScope(properties.getScopes(), properties.getScopes())).toArray(new AuthorizationScope[1]))
                .build();
    }
}