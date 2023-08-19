package com.easyms.sampleapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("easyms.secured.azuread")
@Data
public class RoleAndAuthoritiesMappingProperties {
    private Map<String, List<String>> rolesToAuthorities = new HashMap<>();
    private Map<String, List<String>> rolesToRoles = new HashMap<>();

}
