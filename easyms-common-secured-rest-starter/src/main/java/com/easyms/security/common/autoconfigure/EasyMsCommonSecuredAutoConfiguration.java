package com.easyms.security.common.autoconfigure;


import com.easyms.rest.autoconfigure.EasyMsAutoConfiguration;
import com.easyms.security.common.ms.config.feign.AuthenticationToTokenMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * @author mob.
 */
@Data
@Slf4j
@AutoConfiguration
@ComponentScan(basePackages = {"com.easyms.security.common.ms"})
@AutoConfigureBefore(EasyMsAutoConfiguration.class)
public class EasyMsCommonSecuredAutoConfiguration {

}
