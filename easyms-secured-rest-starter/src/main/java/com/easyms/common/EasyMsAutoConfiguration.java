package com.easyms.common;


import com.easyms.common.ms.log.RequestResponseLogger;
import com.easyms.common.ms.log.SecuredRequestResponseLogger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
    @Primary
    public FilterRegistrationBean<RequestResponseLogger> securedRequestResponseLogger() {
        FilterRegistrationBean<RequestResponseLogger> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SecuredRequestResponseLogger());
        filterRegistrationBean.setOrder(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


}
