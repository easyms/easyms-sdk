package com.easyms.secured;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "easyms.secured", name = "enabled", havingValue = "true")
@ComponentScan(basePackages = {"com.easyms.secured"})
public class EasyMsSecuredAutoConfiguration {

//    @Bean
//    @Primary
//    public FilterRegistrationBean<RequestResponseLogger> securedRequestResponseLogger() {
//        FilterRegistrationBean<RequestResponseLogger> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new SecuredRequestResponseLogger());
//        filterRegistrationBean.setOrder(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }


}
