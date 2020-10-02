//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.easyms.azure.keyvault.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

public class CustomKeyVaultEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final Logger log = LoggerFactory.getLogger(CustomKeyVaultEnvironmentPostProcessor.class);
    public static final int DEFAULT_ORDER = -2147483636;
    private int order = -2147483636;

    public CustomKeyVaultEnvironmentPostProcessor() {
    }

    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (this.isKeyVaultEnabled(environment)) {
            KeyVaultEnvironmentPostProcessorHelper helper = new KeyVaultEnvironmentPostProcessorHelper(environment);
            helper.addKeyVaultPropertySource();
        }

    }

    private boolean isKeyVaultEnabled(ConfigurableEnvironment environment) {
        if (environment.getProperty("azure.keyvault.uri") == null) {
            return false;
        } else {
            return (Boolean)environment.getProperty("azure.keyvault.enabled", Boolean.class, true) && this.isKeyVaultClientAvailable();
        }
    }

    private boolean isKeyVaultClientAvailable() {
        return ClassUtils.isPresent("com.azure.security.keyvault.secrets.SecretClient", CustomKeyVaultEnvironmentPostProcessor.class.getClassLoader());
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
