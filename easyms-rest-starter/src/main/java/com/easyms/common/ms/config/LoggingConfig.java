package com.easyms.common.ms.config;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.easyms.common.utils.LoggingUtils.addLogstashTcpSocketAppender;


/*
 * Configures the Logstash log appenders from the app properties
 */
@Configuration
public class LoggingConfig {

    public LoggingConfig(@Value("${spring.application.name}") String appName,
                         @Value("${server.port}") String serverPort,
                         LogstashProperties logstashProperties,
                         ObjectMapper mapper) throws JsonProcessingException {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        String customFields = mapper.writeValueAsString(map);

        if (logstashProperties.isEnabled()) {
            addLogstashTcpSocketAppender(context, customFields, logstashProperties);
        }
    }
}
