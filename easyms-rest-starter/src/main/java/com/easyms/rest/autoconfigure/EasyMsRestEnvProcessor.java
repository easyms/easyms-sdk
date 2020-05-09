package com.easyms.rest.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsRestEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {

    private final static String AZURE_YML_PROPERTY_FILE = "rest.application.yml";
    @Override
    protected String getYmlFileName() {
        return AZURE_YML_PROPERTY_FILE;
    }
}
