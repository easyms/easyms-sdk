package com.easyms.common.ms.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
public class SecuredRequestResponseLogger extends RequestResponseLogger {

    @Value("${logging.includeUser:true}")
    private boolean includeUser;

    private boolean isIncludeUser() {
        return includeUser || logger.isDebugEnabled();
    }


    protected String extratRequestFrom(HttpServletRequest requestToUse) {
        String from = super.extratRequestFrom(requestToUse);
        if(from.equals(UNKNOWN_USER)) {
          return extractAuthenticatedUser().orElse(UNKNOWN_USER);
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