package com.easyms.security.common.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsCommonSecuredEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {

    private static final String SECURED_YML_PROPERTY_FILE = "common.application.yml";
    @Override
    protected String getYmlFileName() {
        return SECURED_YML_PROPERTY_FILE;
    }
}

