package com.easyms.logging.ms;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.ServletException;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * This workaround manages TraceId logback access logging in case of a first entring request, i.e a request without a TraceId.
 * In this case we rewrite the request with a new traceId
 */
@Component
@ConditionalOnProperty(value = "spring.sleuth.enabled", havingValue = "true", matchIfMissing = true)
public class SleuthLoggingValve extends ValveBase {
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";

    private final Tracer tracer;

    SleuthLoggingValve(Tracer tracer) {
        this.tracer = tracer;
        this.asyncSupported = true;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        addTraceIdToRequestIfMissing(request);
        Valve next = getNext();

        if(next == null) {
            return;
        }

        next.invoke(request, response);
    }

    private void addTraceIdToRequestIfMissing(Request request) {
        String header = request.getHeader(TRACE_ID);
        if(Objects.isNull(header)) {
            org.apache.coyote.Request coyoteRequest = request.getCoyoteRequest();
            MimeHeaders mimeHeaders = coyoteRequest.getMimeHeaders();
            Span span = tracer.spanBuilder().start();

            putHeader(mimeHeaders, TRACE_ID, span.context().traceId());
            putHeader(mimeHeaders, SPAN_ID, span.context().spanId());

        }
    }

    private void putHeader(MimeHeaders mimeHeaders, String headerName, String headerValue) {
        MessageBytes messageBytes = mimeHeaders.addValue(headerName);
        messageBytes.setString(headerValue);
    }
}
