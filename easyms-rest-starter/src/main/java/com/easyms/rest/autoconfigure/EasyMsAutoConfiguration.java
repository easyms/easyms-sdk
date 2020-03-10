package com.easyms.rest.autoconfigure;


import com.easyms.rest.ms.config.SwaggerProperties;
import com.google.common.base.Predicates;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.ZonedDateTime;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.easyms.rest.ms"})
@EnableFeignClients
public class EasyMsAutoConfiguration {

    private final SwaggerProperties properties;

    @Bean
    @ConditionalOnMissingBean({Docket.class})
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
