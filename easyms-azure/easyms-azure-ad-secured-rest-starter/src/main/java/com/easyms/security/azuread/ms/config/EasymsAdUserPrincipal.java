package com.easyms.security.azuread.ms.config;

import com.microsoft.azure.spring.autoconfigure.aad.UserGroup;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Getter;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

public class EasymsAdUserPrincipal extends UserPrincipal {

    @Getter
    private String token;
    private UserPrincipal delegate;


    public EasymsAdUserPrincipal(String token, UserPrincipal userPrincipal) {
        super(null, null);
        this.delegate = userPrincipal;
        this.token = token;
    }

    @Override
    public String getIssuer() {
        return delegate.getIssuer();
    }

    @Override
    public String getSubject() {
        return delegate.getSubject();
    }

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public Object getClaim() {
        return delegate.getClaim();
    }

    @Override
    public Object getClaim(String name) {
        return delegate.getClaim(name);
    }

    @Override
    public String getUpn() {
        return delegate.getUpn();
    }

    @Override
    public String getUniqueName() {
        return delegate.getUniqueName();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getKid() {
        return delegate.getKid();
    }

    @Override
    public void setUserGroups(List<UserGroup> groups) {
        delegate.setUserGroups(groups);
    }

    @Override
    public List<UserGroup> getUserGroups() {
        return delegate.getUserGroups();
    }

    @Override
    public boolean isMemberOf(UserGroup group) {
        return delegate.isMemberOf(group);
    }
}
