package com.easyms.security.azuread.autoconfigure;

import com.easyms.basic.AbstractYmlLoaderEnvPostProcessor;

public class EasyMsAzureAdSecuredEnvProcessor extends AbstractYmlLoaderEnvPostProcessor {

    private final static String AZURE_YML_PROPERTY_FILE = "azuread.application.yml";
    @Override
    protected String getYmlFileName() {
        return AZURE_YML_PROPERTY_FILE;
    }
}
