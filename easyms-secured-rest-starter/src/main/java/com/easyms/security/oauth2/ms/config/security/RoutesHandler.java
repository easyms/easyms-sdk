package com.easyms.security.oauth2.ms.config.security;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author khames.
 */
public interface RoutesHandler {

    String H2_CONSOLE = "/h2-console/**";

    // Swagger
    String SWAGGER_INDEX = "/swagger-ui.html";
    String SWAGGER_RESOURCES = "/swagger-resources/**";
    String SWAGGER_DOCS = "/v2/api-docs/**";
    String SWAGGER_CONFIG = "/configuration/**";
    String SWAGGER_WEBJARS = "/webjars/**";
    String FAVICON_ICO = "/favicon.ico";

    default String[] technicalEndPoints() {
        return new String[]{
                SWAGGER_INDEX,
                SWAGGER_RESOURCES,
                SWAGGER_DOCS,
                SWAGGER_CONFIG,
                SWAGGER_WEBJARS,
                H2_CONSOLE,
                FAVICON_ICO,
                "/csrf"
        };
    }

    default RequestMatcher[] publicEndpoints() {
        return new RequestMatcher[0];
    }

    default List<RequestMatcher> antMatchers(String... antPatterns) {
        return antMatchers(null, antPatterns);
    }

    default List<RequestMatcher> antMatchers(HttpMethod httpMethod, String... antPatterns) {
        String method = Objects.isNull(httpMethod) ? null : httpMethod.toString();
        return ArrayUtils.isEmpty(antPatterns) ? Lists.newArrayList() : Arrays.stream(antPatterns).map(pattern -> new AntPathRequestMatcher(pattern, method)).collect(Collectors.toList());
    }
}
