package com.easyms.sampleapp.config;

import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.properties.AadResourceServerProperties;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import com.easyms.security.azuread.ms.config.CustomJwtGrantedAuthoritiesConverter;
import com.easyms.security.azuread.ms.filter.RawAADAppRolesConverter;
import com.easyms.security.azuread.ms.filter.RoleAndAuthoritiesMappingProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableConfigurationProperties({RoleAndAuthoritiesMappingProperties.class, InternalTokenProperties.class})
@ComponentScan(basePackages = {"com.easyms.sampleapp", "com.easyms.common"})
@EnableJpaRepositories(basePackages = "com.easyms.sampleapp.repository")
public class EasyMsSampleMsConfiguration {

    @Autowired
    private InternalTokenProperties internalTokenProperties;
    @Autowired
    private RoleAndAuthoritiesMappingProperties roleAndAuthoritiesMappingProperties;

    @Bean
    @Primary
    SecurityFilterChain filterChainCustom(HttpSecurity http) throws Exception {
        AadResourceServerHttpSecurityConfigurer aadResourceServerHttpSecurityConfigurer = new AadResourceServerHttpSecurityConfigurer().jwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter(new RawAADAppRolesConverter(roleAndAuthoritiesMappingProperties), AadResourceServerProperties.DEFAULT_CLAIM_TO_AUTHORITY_PREFIX_MAP));
        http.apply(aadResourceServerHttpSecurityConfigurer)
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated();
        return http.build();
    }
}
