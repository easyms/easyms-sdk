package com.easyms.sampleapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.easyms.sampleapp", "com.easyms.common"})
@EnableJpaRepositories(basePackages = "com.easyms.sampleapp.repository")
public class EasyMsSampleMsConfiguration {

}
