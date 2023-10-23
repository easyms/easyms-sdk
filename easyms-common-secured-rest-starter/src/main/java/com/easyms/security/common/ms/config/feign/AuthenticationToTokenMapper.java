package com.easyms.security.common.ms.config.feign;

import org.springframework.security.core.Authentication;

public interface AuthenticationToTokenMapper {
    String map2Token(Authentication authentication);
}
