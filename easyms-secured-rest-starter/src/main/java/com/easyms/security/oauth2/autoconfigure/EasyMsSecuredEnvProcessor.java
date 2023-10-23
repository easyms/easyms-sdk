package com.easyms.security.oauth2.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsSecuredEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {

    private static final String SECURED_YML_PROPERTY_FILE = "secured.application.yml";
    @Override
    protected String getYmlFileName() {
        return SECURED_YML_PROPERTY_FILE;
    }
}

