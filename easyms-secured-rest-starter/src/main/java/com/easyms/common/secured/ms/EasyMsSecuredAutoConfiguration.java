package com.easyms.common.secured.ms;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.easyms.common.secured.ms", "com.easyms.common"})
@EnableResourceServer
public class EasyMsSecuredAutoConfiguration {


}
