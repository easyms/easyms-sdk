package com.easyms.security.common.ms.config.feign;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {
        template.header("requestId", UUID.randomUUID().toString());
        if (!template.headers().containsKey(AUTHORIZATION_HEADER)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.nonNull(auth)) {
                template.header(AUTHORIZATION_HEADER, "%s %s".formatted(BEARER_TOKEN_TYPE, generateToken(auth)));
            }
        }
    }

    private String generateToken(Authentication auth) {
        OAuth2AuthorizationCodeAuthenticationToken details;
        try{
            details = (OAuth2AuthorizationCodeAuthenticationToken) auth.getDetails();
        } catch (Exception exception) {
            Jwt token = ((JwtAuthenticationToken) auth).getToken();
            return Objects.nonNull(token) ? token.getTokenValue() : null;
        }
        return Objects.nonNull(details) ? details.getAccessToken().getTokenValue() : null;
    }

}
