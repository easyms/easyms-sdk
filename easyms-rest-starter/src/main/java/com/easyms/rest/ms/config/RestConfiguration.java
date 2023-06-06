package com.easyms.rest.ms.config;


import com.easyms.rest.ms.error.CommonExceptionHandler;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.core5.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.List;

@Configuration
@Import({JacksonConfig.class, CommonExceptionHandler.class})
public class RestConfiguration {

    private static final int DEFAULT_SERVICE_CONNECT_TIMEOUT = 50000;
    private static final int HTTP_CACHE_MAX_ENTRIES = 1000;
    private static final int HTTP_CACHE_MAX_SIZE = 8192;


    @Bean
    public CacheConfig getCacheConfig() {
        return CacheConfig.custom().setMaxCacheEntries(HTTP_CACHE_MAX_ENTRIES).setMaxObjectSize(HTTP_CACHE_MAX_SIZE).build();
    }

    @Bean
    public HttpClient getHttpClient(CacheConfig cacheConfig) {
        BasicHeader basicHeader = new BasicHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return CachingHttpClients.custom().setCacheConfig(cacheConfig).setDefaultHeaders(List.of(basicHeader)).build();
    }

    @Bean
    public BufferingClientHttpRequestFactory provideBufferingClientHttpRequestFactory(HttpComponentsClientHttpRequestFactory factory) {
        return new BufferingClientHttpRequestFactory(factory);
    }

    @Bean
    @Primary
    public HttpComponentsClientHttpRequestFactory provideHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(DEFAULT_SERVICE_CONNECT_TIMEOUT);
        return factory;
    }

}

