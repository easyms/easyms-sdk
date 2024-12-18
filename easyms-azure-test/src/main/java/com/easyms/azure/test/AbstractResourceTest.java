package com.easyms.azure.test;

import com.azure.spring.cloud.autoconfigure.implementation.aad.filter.UserPrincipalManager;
import jakarta.inject.Inject;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@MockBean(classes = {UserPrincipalManager.class})
@ExtendWith(value = {SecurityTestExtension.class})
@AutoConfigureMockMvc
public class AbstractResourceTest extends AbstractTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Inject
    protected MockMvc mockMvc;

    @Inject
    UserPrincipalManager userPrincipalManager;

    protected RequestPostProcessor bearerToken() {
        return mockHttpServletRequest -> {
            mockHttpServletRequest.addHeader(AUTHORIZATION_HEADER, "%s %s".formatted(BEARER_TOKEN_TYPE, "fake-token"));
            return mockHttpServletRequest;
        };
    }


}
