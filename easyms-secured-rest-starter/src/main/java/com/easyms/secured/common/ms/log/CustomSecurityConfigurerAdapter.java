package com.easyms.secured.common.ms.log;

import com.easyms.common.ms.config.RequestResponseLoggerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.inject.Inject;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


    @Inject
    private RequestResponseLoggerConfig requestResponseLoggerConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new SecuredRequestResponseLogger(requestResponseLoggerConfig), AnonymousAuthenticationFilter.class);
    }
}