package com.easyms.common.ms.log;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author abessa
 */
@Slf4j
public class CustomRequestWrapper extends ContentCachingRequestWrapper {

    @Getter
    private byte[] body;
    @Getter
    private Map<String, String> headerMap = new HashMap<>();

    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            ServletInputStream inputStream = request.getInputStream();
            if (Objects.nonNull(inputStream)) {
                ByteArrayOutputStream cachedContent = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, cachedContent);
                this.body = IOUtils.toByteArray(new ByteArrayInputStream(cachedContent.toByteArray()));
            }
        } catch (IOException e) {
            log.error("error building request wrapper {} ", e.getMessage(), e);
            throw new RuntimeException("error building request wrapper", e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return Objects.nonNull(body) ? new ServletInputStreamWrapper(new ByteArrayInputStream(body)) : null;
    }

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    public void removeHeader(String name) {
        headerMap.remove(name);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        return headerMap.getOrDefault(name, headerValue);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(headerMap.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    public String getRequestURLWithQueryString() {
        return Optional.ofNullable(getQueryString())
                .filter(StringUtils::isNotBlank)
                .map(q -> getRequestURL().toString() + "?" + q)
                .orElse(getRequestURL().toString());
    }
}
