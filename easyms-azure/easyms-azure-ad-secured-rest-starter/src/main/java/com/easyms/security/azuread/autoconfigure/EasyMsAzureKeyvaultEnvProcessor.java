package com.easyms.security.azuread.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class EasyMsAzureKeyvaultEnvProcessor extends AbstractYmlLoaderEnvPostProcessor implements Ordered {

    public static int order = ConfigDataEnvironmentPostProcessor.ORDER;
    private static final String KEYVAULT_YML_PROPERTY_FILE = "keyvault.application.yml";

    @Override
    protected String getYmlFileName() {
        return KEYVAULT_YML_PROPERTY_FILE;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
