package com.easyms.security.azuread.ms.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties("easyms.secured.azuread")
@Data
public class RoleAndAuthoritiesMappingProperties {
    private Map<String, List<String>> rolesToAuthorities = new HashMap<>();
    private Map<String, List<String>> rolesToRoles = new HashMap<>();

}
