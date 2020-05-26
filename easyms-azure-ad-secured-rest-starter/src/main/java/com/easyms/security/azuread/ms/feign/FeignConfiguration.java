package com.easyms.security.azuread.ms.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public FeignInterceptor FeignRequestInterceptor() {
        return new FeignInterceptor();
    }

}
