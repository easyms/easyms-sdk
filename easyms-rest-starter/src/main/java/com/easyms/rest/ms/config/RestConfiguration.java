package com.easyms.rest.ms.config;


import com.easyms.rest.ms.error.CommonExceptionHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Collection;
import java.util.Collections;

@Configuration
@Import({JacksonConfig.class, CommonExceptionHandler.class})
public class RestConfiguration {

    private static final int DEFAULT_SERVICE_READ_TIMEOUT = 30000;
    private static final int DEFAULT_SERVICE_CONNECT_TIMEOUT = 50000;
    private static final int HTTP_CACHE_MAX_ENTRIES = 1000;
    private static final int HTTP_CACHE_MAX_SIZE = 8192;


    @Bean
    public CacheConfig getCacheConfig() {
        return CacheConfig.custom().setMaxCacheEntries(HTTP_CACHE_MAX_ENTRIES).setMaxObjectSize(HTTP_CACHE_MAX_SIZE).build();
    }

    @Bean
    public HttpClient getHttpClient(CacheConfig cacheConfig) {
        BasicHeader basicHeader = new BasicHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        final Collection<BasicHeader> defaultHeaders = Collections.singletonList(basicHeader);
        return CachingHttpClients.custom().setCacheConfig(cacheConfig).setDefaultHeaders(defaultHeaders).build();
    }

    @Bean
    public BufferingClientHttpRequestFactory provideBufferingClientHttpRequestFactory(HttpComponentsClientHttpRequestFactory factory) {
        return new BufferingClientHttpRequestFactory(factory);
    }

    @Bean
    @Primary
    public HttpComponentsClientHttpRequestFactory provideHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(DEFAULT_SERVICE_READ_TIMEOUT);
        factory.setConnectTimeout(DEFAULT_SERVICE_CONNECT_TIMEOUT);
        return factory;
    }

}
