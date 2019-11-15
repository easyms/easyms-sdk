package com.easyms.secured.common.ms.log;

import com.easyms.common.ms.config.RequestResponseLoggerConfig;
import com.easyms.common.ms.log.RequestResponseLogger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public class SecuredRequestResponseLogger extends RequestResponseLogger {


    public SecuredRequestResponseLogger(RequestResponseLoggerConfig requestResponseLoggerConfig) {
        super(requestResponseLoggerConfig);
    }

    private boolean isIncludeUser() {
        return requestResponseLoggerConfig.isIncludeUser() || logger.isDebugEnabled();
    }


    protected String extratRequestFrom(HttpServletRequest requestToUse) {
        String from = super.extratRequestFrom(requestToUse);
        if(from.equals(API_CLIENT)) {
          return extractAuthenticatedUser().orElse(API_CLIENT);
        } else {
            return from;
        }
    }


    private Optional<String> extractAuthenticatedUser() {
        if (isIncludeUser()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return Optional.ofNullable(authentication).map((auth) -> {
                Object principal = auth.getPrincipal();
                String username;
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                return username;
            });
        } else {
            return Optional.empty();
        }
    }

}