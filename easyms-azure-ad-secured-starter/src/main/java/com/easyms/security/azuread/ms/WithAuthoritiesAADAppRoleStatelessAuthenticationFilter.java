package com.easyms.security.azuread.ms;

import com.microsoft.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import net.minidev.json.JSONArray;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithAuthoritiesAADAppRoleStatelessAuthenticationFilter extends AADAppRoleStatelessAuthenticationFilter {
    private static final String PERM_PREFIX = "PERM_";
    private final RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties;

    public WithAuthoritiesAADAppRoleStatelessAuthenticationFilter(UserPrincipalManager principalManager,
                                                                  RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
        super(principalManager);
        this.roleAndAuthoritiesMappingProperties = roleAndAuthoritiesMappingProperties;
    }


    @Override
    protected Set<SimpleGrantedAuthority> rolesToGrantedAuthorities(JSONArray roles) {
        Set<SimpleGrantedAuthority> allAuthorities = super.rolesToGrantedAuthorities(roles);
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .filter(Objects::nonNull)
                .map(s -> Optional.ofNullable(roleAndAuthoritiesMappingProperties.getRolesToAuthorities().get(s)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .map(s -> new SimpleGrantedAuthority(PERM_PREFIX + s))
                .collect(Collectors.toSet());
        allAuthorities.addAll(authorities);
        return allAuthorities;

    }
}
