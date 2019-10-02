package com.easyms.common.ms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LogstashProperties {

    @Value("${easyms.logging.logstash.enabled:}")
    private Boolean enabled = false;
    @Value("${easyms.logging.logstash.host:}")
    private String host = "localhost";
    @Value("${easyms.logging.logstash.port:}")
    private Integer port = 5000;
    @Value("${easyms.logging.logstash.queue-size:}")
    private Integer queueSize = 512;
}
