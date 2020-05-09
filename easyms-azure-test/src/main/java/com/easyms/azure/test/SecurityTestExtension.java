package com.easyms.azure.test;

import com.google.common.collect.Lists;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.when;

public class SecurityTestExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        AbstractResourceTest requiredTestInstance = (AbstractResourceTest) extensionContext.getRequiredTestInstance();
        WithMockUser withMockUserMethod = extensionContext.getRequiredTestMethod().getAnnotation(WithMockUser.class);
        WithMockUser withMockUserClass = extensionContext.getRequiredTestClass().getAnnotation(WithMockUser.class);

        Optional<WithMockUser> withMockUserTarget = Optional.ofNullable(Optional.ofNullable(withMockUserMethod).orElse(withMockUserClass));

        UserPrincipalManager userPrincipalManager = requiredTestInstance.userPrincipalManager;
        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
        Map<String, Object> claims = new HashMap<>();

        JSONArray rolesAndAuthorities = getRolesAndAuthorities(withMockUserTarget.map((userMock) -> Arrays.asList(userMock.authorities())).orElse(newArrayList()),
                withMockUserTarget.map((userMock) -> Arrays.asList(userMock.roles())).orElse(newArrayList()));

        claims.put("roles", rolesAndAuthorities);

        when(userPrincipal.getClaims()).thenReturn(claims);
        when(userPrincipalManager.buildUserPrincipal(Mockito.any())).thenReturn(userPrincipal);

    }



    public JSONArray getRolesAndAuthorities(List<String> authorities, List<String> roles) {
        JSONArray jsonArray = new JSONArray();
        roles.forEach(jsonArray::appendElement);
        authorities.forEach(jsonArray::appendElement);

        return jsonArray;
    }


}
