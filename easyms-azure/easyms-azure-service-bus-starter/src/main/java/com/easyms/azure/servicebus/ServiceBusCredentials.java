package com.easyms.azure.servicebus;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ServiceBusCredentials {
    private String clientId;
    private String clientSecret;
    private String subscriptionId;
    private String tenantId;
    private String activeDirectoryEndpointUrl;
    private String resourceManagerEndpointUrl;
    private String activeDirectoryGraphResourceId;
    private String sqlManagementEndpointUrl;
    private String galleryEndpointUrl;
    private String managementEndpointUrl;
}
