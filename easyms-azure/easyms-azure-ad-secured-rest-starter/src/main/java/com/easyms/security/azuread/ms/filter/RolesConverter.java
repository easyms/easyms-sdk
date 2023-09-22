package com.easyms.security.azuread.ms.filter;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface RolesConverter {
    Set<GrantedAuthority> toSimpleGrantedAuthoritySet(Collection<GrantedAuthority> rawAuthorities);
}
