package com.easyms.security.azuread.ms.config;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadJwtGrantedAuthoritiesConverter;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;

public class CustomJwtGrantedAuthoritiesConverter extends AadJwtGrantedAuthoritiesConverter {

    private RawAADAppRolesConverter rolesConverter;

    public CustomJwtGrantedAuthoritiesConverter(RawAADAppRolesConverter rolesConverter, Map<String, String> claimToAuthorityPrefixMap) {
        super(claimToAuthorityPrefixMap);
        this.rolesConverter = rolesConverter;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = super.convert(jwt);
        return rolesConverter.toSimpleGrantedAuthoritySet(grantedAuthorities);
    }
}
