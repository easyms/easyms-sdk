package com.easyms.security.oauth2.ms.config;


import com.easyms.rest.ms.config.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import lombok.AllArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author abessa
 */
@Configuration
@AllArgsConstructor
public class SecuredSwaggerConfig {
    private static final String SECURITY_SCHEMA_O_AUTH_2 = "oauth2schema";


    private final SwaggerProperties properties;

    @Bean
    public OpenAPI secureAPI() {
        Scopes scopes = new Scopes().addString(properties.getScopes(), properties.getScopes());

        return new OpenAPI()
                .info(new Info().title(properties.getApiInfoTitle())
                        .description(properties.getApiInfoDescription())
                        .version(properties.getApiInfoVersion())
                        .license(new License()
                                .name(properties.getApiInfoLicense())
                                .url(properties.getApiInfoLicenseUrl()))
                        .contact(new Contact()
                                .url(properties.getApiInfoContactURL())
                                .email(properties.getApiInfoContactEmail())
                                .name(properties.getApiInfoContactName())))
                .components(new Components().addSecuritySchemes("spring_oauth", new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .description("Oauth2 Flow")
                        .flows(new OAuthFlows().clientCredentials(new OAuthFlow()
                                .tokenUrl(properties.getTokenUrl())
                                .scopes(scopes)))))
                .security(List.of(new SecurityRequirement().addList("spring_oauth")));
    }

    @Bean
    @ConditionalOnMissingBean({GroupedOpenApi.class})
    public GroupedOpenApi securedSwaggerSpringMvcPlugin() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(properties.getPaths())
                .build();
    }

}
