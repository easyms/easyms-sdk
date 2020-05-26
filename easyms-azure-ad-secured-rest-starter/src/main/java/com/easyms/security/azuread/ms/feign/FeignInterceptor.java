package com.easyms.security.azuread.ms.feign;


import com.easyms.security.azuread.ms.config.EasymsAdUserPrincipal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, generateToken(auth)));
            }
        }
    }

    private String generateToken(Authentication auth) {
        EasymsAdUserPrincipal principal = (EasymsAdUserPrincipal) auth.getPrincipal();
        return Objects.nonNull(principal) ? principal.getToken() : null;
    }

}
