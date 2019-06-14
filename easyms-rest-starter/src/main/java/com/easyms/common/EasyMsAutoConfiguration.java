package com.easyms.common;


import com.easyms.common.ms.config.RestConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author yfo.
 */
@Data
@Slf4j
@Configuration
@Import({RestConfiguration.class
})
@ComponentScan(basePackages = {"com.easyms.common.ms"})
public class EasyMsAutoConfiguration {


}
