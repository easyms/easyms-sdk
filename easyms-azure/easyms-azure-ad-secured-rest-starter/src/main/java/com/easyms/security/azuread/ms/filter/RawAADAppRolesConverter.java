package com.easyms.security.azuread.ms.filter;

import com.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter;
import com.azure.spring.autoconfigure.aad.UserPrincipal;
import com.azure.spring.autoconfigure.aad.UserPrincipalManager;
import net.minidev.json.JSONArray;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RawAADAppRolesConverter {

    private static final String ROLE_PREFIX = "APPROLE_";
    private final RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties;

    @Inject
    public RawAADAppRolesConverter(RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties) {
        this.roleAndAuthoritiesMappingProperties = roleAndAuthoritiesMappingProperties;
    }


    public Set<SimpleGrantedAuthority> toSimpleGrantedAuthoritySet(Collection<GrantedAuthority> rawAuthorities) {

        var normalizeddRoles = normalizeRawRoles(rawAuthorities);

        Set<String> allRoles = Stream.concat(
                normalizeddRoles.stream()
                                .map(s -> Optional.ofNullable(roleAndAuthoritiesMappingProperties.getRolesToRoles().get(s)))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .flatMap(Collection::stream),
                normalizeddRoles.stream())
                .collect(Collectors.toSet());

        return getGrantedAuthorities(allRoles);

    }

    private Set<String> normalizeRawRoles(Collection<GrantedAuthority> rawAuthorities) {
        return rawAuthorities.stream().filter(auth -> {
            return auth.getAuthority().startsWith(ROLE_PREFIX);
        }).map(role -> role.getAuthority().substring(ROLE_PREFIX.length())).collect(Collectors.toSet());
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
