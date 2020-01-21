package com.easyms.common.ms;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.easyms.common.ms", "com.easyms.common"})
@EnableFeignClients
public class EasyMsAutoConfiguration {


}
