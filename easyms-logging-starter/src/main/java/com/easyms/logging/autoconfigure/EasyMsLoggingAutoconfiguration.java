package com.easyms.logging.autoconfigure;


import com.easyms.logging.ms.SleuthLoggingValve;
import jakarta.inject.Inject;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {"com.easyms.logging.ms"})
public class EasyMsLoggingAutoconfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Inject
    private SleuthLoggingValve sleuthLoggingValve;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers(context -> context.getPipeline().addValve(sleuthLoggingValve));
    }
}

