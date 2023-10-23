package com.easyms.rest.autoconfigure;


import com.easyms.rest.ms.config.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author khames.
 */

@AutoConfiguration
@Data
@Slf4j
@ComponentScan(basePackages = {"com.easyms.rest.ms"})
@EnableFeignClients
public class EasyMsAutoConfiguration {

    private final SwaggerProperties properties;

    @Bean
    @ConditionalOnMissingBean({GroupedOpenApi.class})
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(properties.getPaths())
                .build();
    }


    @Bean
    @ConditionalOnMissingBean({OpenAPI.class})
    public OpenAPI openAPI() {
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
                                .name(properties.getApiInfoContactName())));
    }

}

