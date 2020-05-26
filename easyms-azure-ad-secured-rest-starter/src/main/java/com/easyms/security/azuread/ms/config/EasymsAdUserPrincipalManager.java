package com.easyms.security.azuread.ms.config;

import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationProperties;
import com.microsoft.azure.spring.autoconfigure.aad.ServiceEndpointsProperties;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.ResourceRetriever;

import java.text.ParseException;

public class EasymsAdUserPrincipalManager extends UserPrincipalManager {

    public EasymsAdUserPrincipalManager(ServiceEndpointsProperties serviceEndpointsProps,
                                        AADAuthenticationProperties aadAuthProps,
                                        ResourceRetriever resourceRetriever,
                                        boolean explicitAudienceCheck) {
        super(serviceEndpointsProps, aadAuthProps, resourceRetriever, explicitAudienceCheck);
    }

    @Override
    public UserPrincipal buildUserPrincipal(String idToken) throws ParseException, JOSEException, BadJOSEException {
        UserPrincipal delegate = super.buildUserPrincipal(idToken);
        return new EasymsAdUserPrincipal(idToken, delegate);
    }
}
