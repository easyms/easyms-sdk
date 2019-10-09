package com.easyms.common.ms.log;

import lombok.Builder;
import lombok.Data;
import net.logstash.logback.marker.ObjectFieldsAppendingMarker;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;


@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
public class RequestResponseLogger extends OncePerRequestFilter {


    public static final String UNKNOWN_USER = "unknown";
    protected static Logger logger = LoggerFactory.getLogger("flows");


    public static final String EMPTY_PAYLOAD = "";


    @Value("${spring.application.id}")
    private String appId;

    @Value("${logging.includeRequestPayload:true}")
    private boolean includeRequestPayload;

    @Value("${logging.includeResponsePayload:true}")
    private boolean includeResponsePayload;

    @Value("${logging.includeQueryString:true}")
    private boolean includeQueryString;


    /**
     * The default value is "false" so that the filter may log a "before" message
     * at the start of request processing and an "after" message at the end from
     * when the last asynchronously dispatched thread is exiting.
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    /**
     * Forwards the request to the next filter in the chain and delegates down to the subclasses
     * to perform the actual request logging both before and after the request is processed.
     *
     * @see #beforeRequest
     * @see #afterRequest
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (isIncludeRequestPayload() && isFirstRequest && !(request instanceof CustomRequestWrapper)) {
            requestToUse = new CustomRequestWrapper(request);
        }


        if (isIncludeResponsePayload() && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        RequestResponseLogging commonRequestResponseLogging = buildCommontRequestResponseLogging(requestToUse);

        boolean shouldLog = shouldLog(requestToUse);
        if (shouldLog && isFirstRequest) {
            beforeRequest(requestToUse, commonRequestResponseLogging);
        }
        try {
            long start = System.currentTimeMillis();
            filterChain.doFilter(requestToUse, responseToUse);
            commonRequestResponseLogging.elapsed = System.currentTimeMillis() - start;
        } finally {
            if (shouldLog && !isAsyncStarted(requestToUse)) {
                afterRequest(requestToUse, commonRequestResponseLogging, responseToUse);
                if (responseToUse instanceof ContentCachingResponseWrapper){
                    ((ContentCachingResponseWrapper)responseToUse).copyBodyToResponse();
                }
            }
        }
    }

    private RequestResponseLogging buildCommontRequestResponseLogging(HttpServletRequest requestToUse) {
        return RequestResponseLogging.builder()
                .from(extratRequestFrom(requestToUse))
                .to(Optional.ofNullable(appId).orElse("Unknown"))
                .url(extractRequestURL(requestToUse))
                .flowType("rest").build();
    }

    private String extractRequestURL(HttpServletRequest request) {
        if (request instanceof CustomRequestWrapper) {
            return isIncludeQueryString() ? ((CustomRequestWrapper) request).getRequestURLWithQueryString() : request.getRequestURL().toString();
        } else {
            return request.getRequestURL().toString();
        }
    }

    private boolean isIncludeQueryString() {
        return includeQueryString || logger.isDebugEnabled();
    }


    private boolean isIncludeResponsePayload() {
        return includeResponsePayload || logger.isDebugEnabled();
    }

    protected String extratRequestFrom(HttpServletRequest requestToUse) {
        String from = requestToUse.getHeader("FROM");
        if (from != null) {
            return from;
        } else {
            return UNKNOWN_USER;
        }
    }




    /**
     * Determine whether to call the {@link #beforeRequest}/{@link #afterRequest}
     * methods for the current request, i.e. whether logging is currently active
     * (and the log message is worth building).
     * <p>The default implementation always returns {@code true}. Subclasses may
     * override this with a log level check.
     *
     * @param request current HTTP request
     * @return {@code true} if the before/after method should get called;
     * {@code false} otherwise
     * @since 4.1.5
     */
    protected boolean shouldLog(HttpServletRequest request) {
        return true;
    }

    /**
     * Concrete subclasses should implement this method to write a log message
     * <i>before</i> the request is processed.
     *
     * @param request current HTTP request
     * @param logging the message to log
     */
    protected void beforeRequest(HttpServletRequest request, RequestResponseLogging logging) {
        logging.setFlowWay("request");
        logging.setPayload(extractRequestBody(request));
        logger.info(new ObjectFieldsAppendingMarker(logging), "request");
    }

    /**
     * Concrete subclasses should implement this method to write a log message
     * <i>after</i> the request is processed.
     *
     * @param request current HTTP request
     * @param logging the message to log
     */
    protected void afterRequest(HttpServletRequest request, RequestResponseLogging logging, HttpServletResponse response) {
        logging.setFlowWay("response");
        logging.setPayload(extractResponseBody(response));
        logger.info(new ObjectFieldsAppendingMarker(logging), "response");

    }


    private String extractRequestBody(HttpServletRequest request) {
        if (request instanceof CustomRequestWrapper && isIncludeRequestPayload()) {
            String body = this.extractBodyAsString(((CustomRequestWrapper) request).getBody());
            return body;
        } else return EMPTY_PAYLOAD;


    }

    private boolean isIncludeRequestPayload() {
        return includeRequestPayload || logger.isDebugEnabled();
    }

    private String extractResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper && isIncludeResponsePayload()) {
            String body = this.extractBodyAsString(((ContentCachingResponseWrapper)response).getContentAsByteArray());
            return body;
        } else return EMPTY_PAYLOAD;
    }

    private String extractBodyAsString(byte[] body) {
        try {
            return ArrayUtils.isEmpty(body) ? "" : new String(body, Charset.defaultCharset().name());
        } catch (IOException ex) {
            return EMPTY_PAYLOAD;
        }
    }


    @Data
    @Builder
    private static class RequestResponseLogging {
        private String from;
        private String to;
        private String url;
        private String payload;
        private long elapsed;
        private int httpCode;
        private String flowType;
        private String flowWay;
    }
}