package com.easyms.test;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import javax.inject.Inject;

@MockBean(classes = {BearerTokenExtractor.class, AuthenticationManager.class})
@ExtendWith(value = {SecurityTestExtension.class})
@AutoConfigureMockMvc
public class AbstractResourceTest extends AbstractTest{

    @Inject
    protected MockMvc mockMvc;


    @Inject
    TokenExtractor tokenExtractor;

    @Inject
    AuthenticationManager authenticationManager;





    @Component
    @AllArgsConstructor
    static class OAuth2AuthenticationProcessingFilterProcessor implements BeanPostProcessor {
        private final TokenExtractor tokenExtractor;
        private final AuthenticationManager authenticationManager;


        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            Object result = bean;
            if(bean instanceof OAuth2AuthenticationProcessingFilter) {
                OAuth2AuthenticationProcessingFilter oAuth2AuthenticationProcessingFilter = (OAuth2AuthenticationProcessingFilter) bean;
                oAuth2AuthenticationProcessingFilter.setTokenExtractor(tokenExtractor);
                oAuth2AuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
            }
            return result;
        }
    }



}
