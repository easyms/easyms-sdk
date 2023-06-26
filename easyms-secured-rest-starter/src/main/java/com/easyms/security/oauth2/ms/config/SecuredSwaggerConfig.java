package com.easyms.security.oauth2.ms.config;


import com.easyms.rest.ms.config.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author abessa
 */
@Configuration
public class SecuredSwaggerConfig {

    private final SwaggerProperties properties;

    public SecuredSwaggerConfig(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI secureAPI() {

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
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );

    }

    @Bean
    public GroupedOpenApi securedSwaggerSpringMvcPlugin() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(properties.getPaths())
                .build();
    }

}
