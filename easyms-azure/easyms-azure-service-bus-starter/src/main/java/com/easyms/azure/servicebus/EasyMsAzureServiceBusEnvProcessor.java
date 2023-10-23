package com.easyms.azure.servicebus;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsAzureServiceBusEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {


    private static final String SERVICEBUS_APPLICATION_YML = "servicebus.application.yml";
    @Override
    protected String getYmlFileName() {
        return SERVICEBUS_APPLICATION_YML;
    }
}
