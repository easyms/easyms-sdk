package com.easyms.security.azuread.ms.filter;

import jakarta.inject.Inject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RawAADAppRolesConverter implements RolesConverter{

    private static final String ROLE_PREFIX = "APPROLE_";
    private static final String SCOPE_PREFIX = "SCOPE_";
    private final RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties;

    public RawAADAppRolesConverter(RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
        this.roleAndAuthoritiesMappingProperties = roleAndAuthoritiesMappingProperties;
    }


    @Override
    public Set<GrantedAuthority> toSimpleGrantedAuthoritySet(Collection<GrantedAuthority> rawAuthorities) {

        var normalizedRoles = normalizeRawRolesAndScope(rawAuthorities);

        Set<String> allRoles = Stream.concat(
                normalizedRoles.stream()
                                .map(s -> Optional.ofNullable(roleAndAuthoritiesMappingProperties.getRolesToRoles().get(s)))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .flatMap(Collection::stream),
                normalizedRoles.stream())
                .collect(Collectors.toSet());

        return getGrantedAuthorities(allRoles);

    }

    private Set<String> normalizeRawRoles(Collection<GrantedAuthority> rawAuthorities) {
        return rawAuthorities.stream().filter(auth -> {
            return auth.getAuthority().startsWith(ROLE_PREFIX);
        }).map(role -> role.getAuthority().substring(ROLE_PREFIX.length())).collect(Collectors.toSet());
    }

    private Set<String> normalizeRawRolesAndScope(Collection<GrantedAuthority> rawAuthorities) {
        return rawAuthorities.stream().filter(auth -> {
            return auth.getAuthority().startsWith(ROLE_PREFIX) || auth.getAuthority().startsWith(SCOPE_PREFIX);
        }).map(role ->  role.getAuthority().startsWith(ROLE_PREFIX) ? role.getAuthority().substring(ROLE_PREFIX.length()) : role.getAuthority().substring(SCOPE_PREFIX.length())).collect(Collectors.toSet());
    }
    private Set<GrantedAuthority> getGrantedAuthorities(Set<String> roles) {
        //add roles as Authorities
        Set<GrantedAuthority> allAuthorities = roles.stream()
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        //Map roles to authorities
        Set<GrantedAuthority> authorities = roles.stream()
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
