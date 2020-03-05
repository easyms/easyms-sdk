package com.easyms.security.azuread.ms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("easyms.secured.azuread")
@Data
public class RoleAndAuthoritiesMappingProperties {
    private String titi;
    private Map<String, List<String>> rolesToAuthorities = new HashMap<>();;

}
