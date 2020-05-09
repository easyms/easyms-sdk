package com.easyms.security.azuread.ms.filter;

import com.microsoft.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import net.minidev.json.JSONArray;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithAuthoritiesAADAppRoleStatelessAuthenticationFilter extends AADAppRoleStatelessAuthenticationFilter {
    private static final String PERM_PREFIX = "PERM_";
    private static final String ROLE_PREFIX = "ROLE";
    private final RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties;

    public WithAuthoritiesAADAppRoleStatelessAuthenticationFilter(UserPrincipalManager principalManager,
                                                                  RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
        super(principalManager);
        this.roleAndAuthoritiesMappingProperties = roleAndAuthoritiesMappingProperties;
    }


    @Override
    protected Set<SimpleGrantedAuthority> rolesToGrantedAuthorities(JSONArray roles) {

        //add linked roles to initial roles
        Set<String> initialRoles = roles.stream()
                .map(r -> (String) r)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> allRoles = Stream.concat(
                initialRoles.stream()
                        .map(s -> Optional.ofNullable(roleAndAuthoritiesMappingProperties.getRolesToRoles().get(s)))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .flatMap(Collection::stream),
                initialRoles.stream())
                .collect(Collectors.toSet());

        return getGrantedAuthorities(allRoles);

    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(Set<String> roles) {
        //add roles as Authorities
        Set<SimpleGrantedAuthority> allAuthorities = roles.stream()
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        //Map roles to authorities
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(r -> (String) r)
                .filter(Objects::nonNull)
                .map(s -> Optional.ofNullable(roleAndAuthoritiesMappingProperties.getRolesToAuthorities().get(s)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        allAuthorities.addAll(authorities);

        return allAuthorities;
    }
}
