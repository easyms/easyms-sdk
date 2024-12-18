package com.easyms.sampleapp.api;


import com.easyms.test.TestJWTUtils;
import feign.Contract;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;


public class ClientFeignConfiguration {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";


    @Bean
    public Encoder encoder() {
        return new SpringFormEncoder();
    }

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    //Needed for test purpose, replace with a valid token before testing
    @Bean
    public RequestInterceptor FeignRequestInterceptor() {
        return requestTemplate -> requestTemplate.header(AUTHORIZATION_HEADER, "%s %s".formatted(BEARER_TOKEN_TYPE, TestJWTUtils.encode("any")));
    }


}

