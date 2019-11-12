package com.easyms.common.ms.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
public class RequestResponseLoggerConfig {

    @Value("${spring.application.id}")
    private String appId;

    @Value("${logging.includeRequestPayload:true}")
    private boolean includeRequestPayload;

    @Value("${logging.includeResponsePayload:" + IncludePayloadConfig.ALWAYS + "}")
    private String includeResponsePayload;

    @Value("${logging.includeQueryString:true}")
    private boolean includeQueryString;

    @Value("${logging.includeUser:true}")
    private boolean includeUser;

    public interface IncludePayloadConfig {
        String NONE = "none";
        String ONLY_ON_ERROR = "onlyOnError";
        String ALWAYS = "always";

    }
}
