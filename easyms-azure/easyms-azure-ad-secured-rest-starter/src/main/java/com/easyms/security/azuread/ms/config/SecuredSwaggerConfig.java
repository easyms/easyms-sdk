package com.easyms.security.azuread.ms.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import lombok.AllArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

/**
 * @author abessa
 */
@Configuration
@AllArgsConstructor
public class SecuredSwaggerConfig {


    private final AzureAdSwaggerProperties properties;

    private Info getInfo() {
        return new Info().title(properties.getApiInfoTitle())
                .description(properties.getApiInfoDescription())
                .version(properties.getApiInfoVersion())
                .license(new License()
                        .name(properties.getApiInfoLicense())
                        .url(properties.getApiInfoLicenseUrl()))
                .contact(new Contact()
                        .url(properties.getApiInfoContactURL())
                        .email(properties.getApiInfoContactEmail())
                        .name(properties.getApiInfoContactName()));
    }

    @Bean
    public OpenAPI secureAPI() {
        Scopes scopes = new Scopes();
        properties.getScopes().forEach(s -> scopes.addString(s, s));
        return new OpenAPI()
                .info(getInfo())
                .components(new Components().addSecuritySchemes("codeSchema", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("code Authentication")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(properties.getTokenRequestUrl())
                                                .tokenUrl(properties.getTokenUrl())
                                                .scopes(scopes))))
                )
                .security(singletonList(new SecurityRequirement().addList("codeSchema")));
    }

    @Bean
    public GroupedOpenApi securedSwaggerSpringMvcPlugin() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(properties.getPaths())
                .build();
    }

}
