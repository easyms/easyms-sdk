package com.easyms.common;


import com.easyms.common.ms.config.RequestResponseLoggerConfig;
import com.easyms.common.ms.log.RequestResponseLogger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.easyms.common.ms"})
public class EasyMsAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "easyms.secured", name = "enabled", havingValue = "false", matchIfMissing = true)
    public FilterRegistrationBean<RequestResponseLogger> requestResponseLogger(RequestResponseLoggerConfig requestResponseLoggerConfig) {
        FilterRegistrationBean<RequestResponseLogger> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RequestResponseLogger(requestResponseLoggerConfig));
        filterRegistrationBean.setOrder(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
