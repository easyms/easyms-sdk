package com.easyms.logging.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsLoggingEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {

    private final static String LOGGING_YML_PROPERTY_FILE = "logging.application.yml";
    @Override
    protected String getYmlFileName() {
        return LOGGING_YML_PROPERTY_FILE;
    }
}
