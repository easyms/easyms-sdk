package com.easyms.security.azuread.ms.config;

import com.azure.spring.aad.AADJwtGrantedAuthoritiesConverter;
import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import com.azure.spring.aad.implementation.constants.AADTokenClaim;
import com.azure.spring.aad.implementation.constants.AuthorityPrefix;
import com.azure.spring.aad.webapi.AADJwtBearerTokenAuthenticationConverter;
import com.azure.spring.aad.webapi.AADResourceServerProperties;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CustomAADJwtBearerTokenAuthenticationConverter extends AADJwtBearerTokenAuthenticationConverter {

    private RawAADAppRolesConverter rolesConverter;

    /**
     * Construct AADJwtBearerTokenAuthenticationConverter by AADTokenClaim.SUB and
     * DEFAULT_CLAIM_TO_AUTHORITY_PREFIX_MAP.
     */
    public CustomAADJwtBearerTokenAuthenticationConverter() {
        super(AADTokenClaim.SUB, AADResourceServerProperties.DEFAULT_CLAIM_TO_AUTHORITY_PREFIX_MAP);
    }

    /**
     * Construct AADJwtBearerTokenAuthenticationConverter with the authority claim.
     *
     * @param authoritiesClaimName authority claim name
     */
    public CustomAADJwtBearerTokenAuthenticationConverter(String authoritiesClaimName) {
        super(authoritiesClaimName, AuthorityPrefix.SCOPE);
    }

    /**
     * Construct AADJwtBearerTokenAuthenticationConverter with the authority claim name and prefix.
     *
     * @param authoritiesClaimName authority claim name
     * @param authorityPrefix the prefix name of the authority
     */
    public CustomAADJwtBearerTokenAuthenticationConverter(String authoritiesClaimName,
                                                    String authorityPrefix) {
       super(authoritiesClaimName, authorityPrefix);
    }

    /**
     * Using spring security provides JwtGrantedAuthoritiesConverter, it can resolve the access token of scp or roles.
     *
     * @param principalClaimName authorities claim name
     * @param claimToAuthorityPrefixMap the authority name and prefix map
     */
    public CustomAADJwtBearerTokenAuthenticationConverter(String principalClaimName,
                                                          Map<String, String> claimToAuthorityPrefixMap,
                                                          RawAADAppRolesConverter rolesConverter) {
        this.rolesConverter = rolesConverter;
        Assert.notNull(claimToAuthorityPrefixMap, "claimToAuthorityPrefixMap cannot be null");

    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        var authenticationToken = super.convert(jwt);

        var authorities = rolesConverter.toSimpleGrantedAuthoritySet(authenticationToken.getAuthorities());
        var principal = (OAuth2AuthenticatedPrincipal) authenticationToken.getPrincipal();

        OAuth2AuthenticatedPrincipal newPrincipal = new EasymsAdUserPrincipal(authorities, principal);

        var bearerTokenAuthentication = new BearerTokenAuthentication(newPrincipal,
                (OAuth2AccessToken) authenticationToken.getCredentials(),  authorities);
        return bearerTokenAuthentication;
    }


}
