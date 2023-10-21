package com.easyms.azure.servicebus;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServiceBusContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    public static int order = Integer.MAX_VALUE;
    public static String AZURE_CREDENTIALS_FILE = "easyms.spring.cloud.azure.servicebus.credentials.file";

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        ServiceBusCredentials serviceBusCredentials = loadServiceBusCredentials(AZURE_CREDENTIALS_FILE, environment);
        String onFlightProperties = "onFlightAzureBusProperties";
        if (isNotYetInitializedAzureBusProperties(environment, onFlightProperties) &&
                serviceBusCredentials.getClientId() != null && serviceBusCredentials.getClientSecret() != null) {

            Map<String, Object> myMap = new HashMap<>();
            myMap.put("spring.cloud.azure.credential.client-id", serviceBusCredentials.getClientId());
            myMap.put("spring.cloud.azure.credential.client-secret", serviceBusCredentials.getClientSecret());
            myMap.put("spring.cloud.azure.profile.tenant-id", serviceBusCredentials.getTenantId());
            myMap.put("spring.cloud.azure.profile.subscription-id", serviceBusCredentials.getSubscriptionId());
            environment.getPropertySources().addLast(new MapPropertySource(onFlightProperties, myMap));
        }

    }

    private boolean isNotYetInitializedAzureBusProperties(ConfigurableEnvironment environment, String onFlightProperties) {
        return environment.getProperty("spring.cloud.azure.credential.client-id") == null;
    }

    private String getAzureCredentialsProperty(String propertyKey, ConfigurableEnvironment environment) {
        return environment.getProperty(AZURE_CREDENTIALS_FILE + "." + propertyKey);
    }


    private ServiceBusCredentials loadServiceBusCredentials(String basePath, ConfigurableEnvironment environment) {
        return ServiceBusCredentials.builder()
                .clientId(getAzureCredentialsProperty("clientId", environment))
                .clientSecret(getAzureCredentialsProperty("clientSecret", environment))
                .subscriptionId(getAzureCredentialsProperty("subscriptionId", environment))
                .tenantId(getAzureCredentialsProperty("tenantId", environment))
                .activeDirectoryEndpointUrl(getAzureCredentialsProperty("activeDirectoryEndpointUrl", environment))
                .galleryEndpointUrl(getAzureCredentialsProperty("galleryEndpointUrl", environment))
                .managementEndpointUrl(getAzureCredentialsProperty("managementEndpointUrl", environment))
                .resourceManagerEndpointUrl(getAzureCredentialsProperty("resourceManagerEndpointUrl", environment))
                .sqlManagementEndpointUrl(getAzureCredentialsProperty("sqlManagementEndpointUrl", environment))
                .activeDirectoryGraphResourceId(getAzureCredentialsProperty("activeDirectoryGraphResourceId", environment))
                .build();
    }


    private Optional<String> getCredentialFilePresent(ConfigurableEnvironment environment) {
        return Optional.ofNullable(environment.getProperty(AZURE_CREDENTIALS_FILE));
    }

    @Override
    public int getOrder() {
        return order;
    }
}
