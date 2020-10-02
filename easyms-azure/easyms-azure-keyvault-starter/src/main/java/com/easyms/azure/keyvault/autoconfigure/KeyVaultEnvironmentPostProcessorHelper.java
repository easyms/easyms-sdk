//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.easyms.azure.keyvault.autoconfigure;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientCertificateCredentialBuilder;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import com.microsoft.azure.telemetry.TelemetryData;
import com.microsoft.azure.telemetry.TelemetrySender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

class KeyVaultEnvironmentPostProcessorHelper {
    private static final Logger log = LoggerFactory.getLogger(KeyVaultEnvironmentPostProcessorHelper.class);
    private final ConfigurableEnvironment environment;

    public KeyVaultEnvironmentPostProcessorHelper(ConfigurableEnvironment environment) {
        this.environment = environment;
        this.sendTelemetry();
    }

    public void addKeyVaultPropertySource() {
        String vaultUri = this.getProperty(this.environment, "azure.keyvault.uri");
        Long refreshInterval = (Long)Optional.ofNullable(this.environment.getProperty("azure.keyvault.refresh-interval")).map(Long::valueOf).orElse(1800000L);
        Binder binder = Binder.get(this.environment);
        List<String> secretKeys = (List)binder.bind("azure.keyvault.secret.keys", Bindable.listOf(String.class)).orElse(Collections.emptyList());
        TokenCredential tokenCredential = this.getCredentials();
        SecretClient secretClient = (new SecretClientBuilder()).vaultUrl(vaultUri).credential(tokenCredential).buildClient();

        try {
            MutablePropertySources sources = this.environment.getPropertySources();
            KeyVaultOperation kvOperation = new KeyVaultOperation(secretClient, vaultUri, refreshInterval, secretKeys);
            if (sources.contains("systemEnvironment")) {
                sources.addAfter("systemEnvironment", new CustomKeyVaultPropertySource(kvOperation));
            } else {
                sources.addFirst(new CustomKeyVaultPropertySource(kvOperation));
            }

        } catch (Exception var9) {
            throw new IllegalStateException("Failed to configure KeyVault property source", var9);
        }
    }

    public TokenCredential getCredentials() {
        String clientId;
        String certPath;
        if (this.environment.containsProperty("azure.keyvault.client-id") && this.environment.containsProperty("azure.keyvault.client-key") && this.environment.containsProperty("azure.keyvault.tenant-id")) {
            log.debug("Will use custom credentials");
            clientId = this.getProperty(this.environment, "azure.keyvault.client-id");
            certPath = this.getProperty(this.environment, "azure.keyvault.client-key");
            String tenantId = this.getProperty(this.environment, "azure.keyvault.tenant-id");
            ClientSecretCredential clientSecretCredential = ((ClientSecretCredentialBuilder)((ClientSecretCredentialBuilder)(new ClientSecretCredentialBuilder()).clientId(clientId)).clientSecret(certPath).tenantId(tenantId)).build();
            return clientSecretCredential;
        } else if (this.environment.containsProperty("azure.keyvault.client-id") && this.environment.containsProperty("azure.keyvault.certificate.path") && this.environment.containsProperty("azure.keyvault.tenant-id")) {
            clientId = this.environment.getProperty("azure.keyvault.certificate.password");
            certPath = this.getProperty(this.environment, "azure.keyvault.certificate.path");
            return StringUtils.isEmpty(clientId) ? ((ClientCertificateCredentialBuilder)((ClientCertificateCredentialBuilder)(new ClientCertificateCredentialBuilder()).tenantId(this.getProperty(this.environment, "azure.keyvault.tenant-id"))).clientId(this.getProperty(this.environment, "azure.keyvault.client-id"))).pemCertificate(certPath).build() : ((ClientCertificateCredentialBuilder)((ClientCertificateCredentialBuilder)(new ClientCertificateCredentialBuilder()).tenantId(this.getProperty(this.environment, "azure.keyvault.tenant-id"))).clientId(this.getProperty(this.environment, "azure.keyvault.client-id"))).pfxCertificate(certPath, clientId).build();
        } else if (this.environment.containsProperty("azure.keyvault.client-id")) {
            log.debug("Will use MSI credentials with specified clientId");
            clientId = this.getProperty(this.environment, "azure.keyvault.client-id");
            return (new ManagedIdentityCredentialBuilder()).clientId(clientId).build();
        } else {
            log.debug("Will use MSI credentials");
            return (new ManagedIdentityCredentialBuilder()).build();
        }
    }

    private String getProperty(ConfigurableEnvironment env, String propertyName) {
        Assert.notNull(env, "env must not be null!");
        Assert.notNull(propertyName, "propertyName must not be null!");
        String property = env.getProperty(propertyName);
        if (property != null && !property.isEmpty()) {
            return property;
        } else {
            throw new IllegalArgumentException("property " + propertyName + " must not be null");
        }
    }

    private boolean allowTelemetry(ConfigurableEnvironment env) {
        Assert.notNull(env, "env must not be null!");
        return (Boolean)env.getProperty("azure.keyvault.allow.telemetry", Boolean.class, true);
    }

    private void sendTelemetry() {
        if (this.allowTelemetry(this.environment)) {
            Map<String, String> events = new HashMap();
            TelemetrySender sender = new TelemetrySender();
            events.put("serviceName", TelemetryData.getClassPackageSimpleName(KeyVaultEnvironmentPostProcessorHelper.class));
            sender.send(ClassUtils.getUserClass(this.getClass()).getSimpleName(), events);
        }

    }
}
