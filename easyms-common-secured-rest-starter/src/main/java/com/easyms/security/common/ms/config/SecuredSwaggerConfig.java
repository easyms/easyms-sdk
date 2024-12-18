package com.easyms.security.common.ms.config;


import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.rest.ms.config.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import lombok.AllArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

/**
 * @author mob.
 */
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
public class SecuredSwaggerConfig {


    private final SwaggerProperties properties;

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
    @ConditionalOnMissingBean({OpenAPI.class})
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
    @ConditionalOnMissingBean({GroupedOpenApi.class})
    public GroupedOpenApi securedSwaggerSpringMvcPlugin() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(properties.getPaths())
                .build();
    }

}
