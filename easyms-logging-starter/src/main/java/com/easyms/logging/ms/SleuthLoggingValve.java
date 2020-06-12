package com.easyms.logging.ms;

import brave.Span;
import brave.Tracer;
import lombok.AllArgsConstructor;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Objects;

/**
 * This workaround manages TraceId logback access logging in case of a first entring request, i.e a request without a TraceId.
 * In this case we rewrite the request with a new traceId
 */
@Component
@ConditionalOnProperty(value = "spring.sleuth.enabled", havingValue = "true", matchIfMissing = true)
public class SleuthLoggingValve extends ValveBase {
    public static final String X_B3_TRACE_ID = "X-B3-TraceId";
    public static final String X_B3_SPAN_ID = "X-B3-SpanId";

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
        String header = request.getHeader(X_B3_TRACE_ID);
        if(Objects.isNull(header)) {
            org.apache.coyote.Request coyoteRequest = request.getCoyoteRequest();
            MimeHeaders mimeHeaders = coyoteRequest.getMimeHeaders();
            Span span = tracer.newTrace();

            putHeader(mimeHeaders, X_B3_TRACE_ID, span.context().traceIdString());
            putHeader(mimeHeaders, X_B3_SPAN_ID, span.context().traceIdString());

        }
    }

    private void putHeader(MimeHeaders mimeHeaders, String headerName, String headerValue) {
        MessageBytes messageBytes = mimeHeaders.addValue(headerName);
        messageBytes.setString(headerValue);
    }
}
