package com.easyms.common.ms.config.security;

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

    // Spring boot
    String SPRING_BOOT_ACTUATOR = "/actuator/**";
    String SPRING_BOOT_AUDIT_EVENTS = "/auditevents";
    String SPRING_BOOT_AUTOCONFIG = "/autoconfig";
    String SPRING_BOOT_BEANS = "/beans";
    String SPRING_BOOT_CONFIGPROPS = "/configprops";
    String SPRING_BOOT_DUMP = "/dump";
    String SPRING_BOOT_ENV = "/env";
    String SPRING_BOOT_HEALTH = "/health";
    String SPRING_BOOT_INFO = "/info";
    String SPRING_BOOT_LOGGERS = "/loggers";
    String SPRING_BOOT_LOGGERS_ = "/loggers/**";
    String SPRING_BOOT_METRICS = "/metrics";
    String SPRING_BOOT_METRICS_ = "/metrics/**";
    String SPRING_BOOT_MAPPINGS = "/mappings";
    String SPRING_BOOT_TRACE = "/trace";
    String SPRING_BOOT_JOLOKIA = "/jolokia";
    String SPRING_BOOT_JOLOKIA_ = "/jolokia/**";
    String SPRING_BOOT_LIQUIBASE = "/liquibase";
    String SPRING_BOOT_FLYWAY = "/flyway";
    String SPRING_BOOT_LOG = "/logfile";
    String SPRING_BOOT_HEAP_DUMP = "/heapdump";
    String SPRING_BOOT_DOCS = "/docs";
    String SPRING_BOOT_ICON = "/icon.svg";
    String SPRING_BOOT_DEPENDENCIES = "/dependencies";
    String SPRING_BOOT_REGISTRY = "/service-registry";
    String SPRING_BOOT_ARCHAIUS = "/archaius";
    String SPRING_BOOT_FEATURES = "/features";
    String SPRING_BOOT_PROXY_STREAM = "/proxy.stream";
    String SPRING_BOOT_HYSTRIX_STREAM = "/hystrix.stream";
    String SPRING_BOOT_HYSTRIX = "/hystrix/**";

    // Swagger
    String SWAGGER_INDEX = "/swagger-ui.html";
    String SWAGGER_RESOURCES = "/swagger-resources/**";
    String SWAGGER_DOCS = "/v2/api-docs/**";
    String SWAGGER_CONFIG = "/configuration/**";
    String SWAGGER_WEBJARS = "/webjars/**";

    default String[] technicalEndPoints() {
        return new String[]{
                SPRING_BOOT_ACTUATOR,
                SPRING_BOOT_AUDIT_EVENTS,
                SPRING_BOOT_AUTOCONFIG,
                SPRING_BOOT_BEANS,
                SPRING_BOOT_CONFIGPROPS,
                SPRING_BOOT_DUMP,
                SPRING_BOOT_ENV,
                SPRING_BOOT_HEALTH,
                SPRING_BOOT_INFO,
                SPRING_BOOT_LOGGERS,
                SPRING_BOOT_LOGGERS_,
                SPRING_BOOT_METRICS,
                SPRING_BOOT_METRICS_,
                SPRING_BOOT_MAPPINGS,
                SPRING_BOOT_TRACE,
                SPRING_BOOT_JOLOKIA,
                SPRING_BOOT_JOLOKIA_,
                SPRING_BOOT_LIQUIBASE,
                SPRING_BOOT_FLYWAY,
                SPRING_BOOT_LOG,
                SPRING_BOOT_HEAP_DUMP,
                SPRING_BOOT_DOCS,
                SPRING_BOOT_ICON,
                SPRING_BOOT_DEPENDENCIES,
                SPRING_BOOT_REGISTRY,
                SPRING_BOOT_ARCHAIUS,
                SPRING_BOOT_FEATURES,
                SWAGGER_INDEX,
                SWAGGER_RESOURCES,
                SWAGGER_DOCS,
                SWAGGER_CONFIG,
                SWAGGER_WEBJARS,
                SPRING_BOOT_HYSTRIX_STREAM,
                SPRING_BOOT_PROXY_STREAM,
                SPRING_BOOT_HYSTRIX,
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
