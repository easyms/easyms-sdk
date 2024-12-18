package com.easyms.security.common.ms.config.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author mob.
 */
public interface RoutesHandler {

    String H2_CONSOLE = "/h2-console/**";

    // Swagger
    String SWAGGER_INDEX = "/swagger-ui.html";
    String SWAGGER_INDEX_HTML = "/swagger-ui/**";
    String SWAGGER_RESOURCES = "/swagger-resources/**";
    String SWAGGER_DOCS = "/v3/api-docs/**";
    String SWAGGER_DOCS_V3 = "/v3/api-docs";
    String SWAGGER_CONFIG = "/configuration/**";
    String SWAGGER_WEBJARS = "/webjars/**";
    String FAVICON_ICO = "/favicon.ico";

    default String[] technicalEndPoints() {
        return new String[]{
                SWAGGER_INDEX,
                SWAGGER_INDEX_HTML,
                SWAGGER_DOCS_V3,
                SWAGGER_RESOURCES,
                SWAGGER_DOCS,
                SWAGGER_CONFIG,
                SWAGGER_WEBJARS,
                H2_CONSOLE,
                FAVICON_ICO,
                "/csrf",
                "/swagger-ui/index.html"
        };
    }

    default RequestMatcher[] publicEndpoints() {
        return new RequestMatcher[0];
    }
}
