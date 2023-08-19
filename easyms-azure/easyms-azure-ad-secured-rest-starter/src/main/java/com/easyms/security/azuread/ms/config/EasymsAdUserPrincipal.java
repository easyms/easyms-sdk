package com.easyms.security.azuread.ms.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class EasymsAdUserPrincipal implements OAuth2AuthenticatedPrincipal {


    private final Set<SimpleGrantedAuthority> authorities;
    private final OAuth2AuthenticatedPrincipal delegate;

    public EasymsAdUserPrincipal(Set<SimpleGrantedAuthority> authorities, OAuth2AuthenticatedPrincipal delegate) {

        this.authorities = authorities;
        this.delegate = delegate;
    }


    @Override
    public <A> A getAttribute(String name) {
        return delegate.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
