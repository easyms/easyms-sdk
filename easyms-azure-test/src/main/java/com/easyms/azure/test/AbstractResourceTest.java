package com.easyms.azure.test;

import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

@MockBean(classes = {UserPrincipalManager.class})
@ExtendWith(value = {SecurityTestExtension.class})
@AutoConfigureMockMvc
public class AbstractResourceTest extends AbstractTest{

    @Inject
    protected MockMvc mockMvc;

    @Inject
    UserPrincipalManager userPrincipalManager;




}
