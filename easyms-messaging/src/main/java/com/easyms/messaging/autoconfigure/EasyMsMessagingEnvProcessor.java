package com.easyms.messaging.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsMessagingEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {

    private static final String PROPERTY_FILE = "messaging.application.yml";
    @Override
    protected String getYmlFileName() {
        return PROPERTY_FILE;
    }
}

