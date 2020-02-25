package com.easyms.test;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

public class SecurityTestExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        AbstractResourceTest requiredTestInstance = (AbstractResourceTest) extensionContext.getRequiredTestInstance();
        WithMockUser withMockUserMethod = extensionContext.getRequiredTestMethod().getAnnotation(WithMockUser.class);
        WithMockUser withMockUserClass = extensionContext.getRequiredTestClass().getAnnotation(WithMockUser.class);

        Optional<WithMockUser> withMockUserTarget = Optional.ofNullable(Optional.ofNullable(withMockUserMethod).orElse(withMockUserClass));

        OAuth2Request storedRequest = new OAuth2Request(new HashMap<>(), "clientId",
                createAuthorityList(withMockUserTarget.map(mockUser -> Arrays.asList(mockUser.roles())).orElse(Lists.newArrayList()),
                        withMockUserTarget.map(mockUser -> Arrays.asList(mockUser.authorities())).orElse(Lists.newArrayList())),
                true, new HashSet<>(), new HashSet<>(), "", new HashSet<>(), new HashMap<>());

        OAuth2Authentication auth2Authentication = new OAuth2Authentication(storedRequest, null);

        Mockito.when(requiredTestInstance.tokenExtractor.extract(Mockito.any()))
                .thenReturn(new PreAuthenticatedAuthenticationToken("aToken", null));

        Mockito.when(requiredTestInstance.authenticationManager.authenticate(Mockito.any())).thenReturn(auth2Authentication);


    }


    public List<GrantedAuthority> createAuthorityList(List<String> roles, List<String> permissions) {
        List<GrantedAuthority> authorities = Optional.ofNullable(roles).orElse(Lists.newArrayList()).stream()
                .filter(StringUtils::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authorities.addAll(Optional.ofNullable(permissions).orElse(Lists.newArrayList()).stream()
                .filter(StringUtils::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        return authorities;
    }

}
