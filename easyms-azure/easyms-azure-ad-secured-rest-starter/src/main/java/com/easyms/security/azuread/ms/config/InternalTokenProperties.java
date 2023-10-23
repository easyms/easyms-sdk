package com.easyms.security.azuread.ms.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("easyms.secured.azuread.internaltoken")
@Data
public class InternalTokenProperties {
    private boolean enabled;
    private String pubKeyPath;
    private String pubKeyValue;
    private String issuer;

    @PostConstruct
    void validate() {
        if (isEnabled()) {
            if (StringUtils.isEmpty((pubKeyPath)) && StringUtils.isEmpty(pubKeyValue)) {
                throw new IllegalArgumentException("Pubkey is required, it should be provided either by path or by value");
            }
            if (StringUtils.isEmpty(issuer)) {
                throw new IllegalArgumentException("issuer is required");
            }
        }
    }
}

