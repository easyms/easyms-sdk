package com.easyms.azure.test;

import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import javax.inject.Inject;

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
            mockHttpServletRequest.addHeader(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, "fake-token"));
            return mockHttpServletRequest;
        };
    }


}
