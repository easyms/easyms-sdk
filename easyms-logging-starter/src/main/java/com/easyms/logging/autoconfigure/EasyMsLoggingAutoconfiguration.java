package com.easyms.logging.autoconfigure;


import com.easyms.logging.ms.SleuthLoggingValve;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {"com.easyms.logging.ms"})
public class EasyMsLoggingAutoconfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private SleuthLoggingValve sleuthLoggingValve;
    @Autowired
    private Tracer tracer;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers(context -> context.getPipeline().addValve(sleuthLoggingValve));
    }
}

