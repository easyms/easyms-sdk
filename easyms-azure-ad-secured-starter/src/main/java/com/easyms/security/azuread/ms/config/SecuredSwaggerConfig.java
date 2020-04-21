package com.easyms.security.azuread.ms.config;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.LoginEndpointBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


    private final AzureAdSwaggerProperties properties;


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
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
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

        List<AuthorizationScope> scopes = getAuthorizationScopes();

//        TokenEndpoint tokenEndpoint = new TokenEndpointBuilder()
//                .url(properties.getTokenUrl())
//                .tokenName("token")
//                .build();
//
//        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpointBuilder()
//                .url(properties.getTokenRequestUrl())
//                .clientIdName(properties.getClientId())
//                .clientSecretName(properties.getClientSecret())
//               // .clientIdName("toto")
//               // .clientSecretName("name")
//                .build();

//        AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint);

        LoginEndpoint loginEndpoint = new LoginEndpointBuilder()
                .url(properties.getTokenRequestUrl())
                .build();

        ImplicitGrant implicitGrant = new ImplicitGrant(loginEndpoint, "token");


        return new OAuth(SECURITY_SCHEMA_O_AUTH_2, scopes, Lists.newArrayList(implicitGrant));
    }

    private List<AuthorizationScope> getAuthorizationScopes() {
        return properties.getScopes().stream().map((s) -> new AuthorizationScope(s, s)).collect(Collectors.toList());
    }

    private SecurityReference securityReference() {
        return SecurityReference.builder()
                .reference(SECURITY_SCHEMA_O_AUTH_2)
                .scopes(getAuthorizationScopes().toArray(new AuthorizationScope[1]))
                .build();
    }
}