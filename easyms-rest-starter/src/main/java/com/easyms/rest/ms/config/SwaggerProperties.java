package com.easyms.rest.ms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class SwaggerProperties {

    @Value("${swagger.paths:}")
    private String paths;

    @Value("${swagger.api.info.title:}")
    private String apiInfoTitle;

    @Value("${swagger.api.info.description:}")
    private String apiInfoDescription;

    @Value("${swagger.api.info.contact.email:}")
    private String apiInfoContactEmail;

    @Value("${swagger.api.info.contact.name:}")
    private String apiInfoContactName;

    @Value("${swagger.api.info.contact.url:}")
    private String apiInfoContactURL;

    @Value("${swagger.api.info.version:}")
    private String apiInfoVersion;

    @Value("${swagger.api.info.license:}")
    private String apiInfoLicense;

    @Value("${swagger.api.info.licenseUrl:}")
    private String apiInfoLicenseUrl;

    @Value("${swagger.securityDefinitions.oauthSecurity.scopes:}")
    private String scopes;

    @Value("${swagger.securityDefinitions.oauthSecurity.tokenUrl:}")
    private String tokenUrl;

}
