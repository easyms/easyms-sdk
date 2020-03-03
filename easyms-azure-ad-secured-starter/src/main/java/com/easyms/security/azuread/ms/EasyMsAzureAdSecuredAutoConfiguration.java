package com.easyms.security.azuread.ms;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author khames.
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(value="easyms.secured.azuread", havingValue = "true")
@ComponentScan
public class EasyMsAzureAdSecuredAutoConfiguration {


    @Bean("toto")
    String provideData() {
        return "toto";
    }


}
