package com.easyms.messaging.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.function.Function;

@AutoConfiguration
@ComponentScan
@Slf4j
public class EasyMsMessagingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "publisherConfirmsHandler")
    public Function<Message<?>, MessageHandler> publisherConfirmsHandler() {
        return msg -> {
            log.info("acknowledged message {}", msg);
            return null;
        };
    }

    @Bean
    @ConditionalOnMissingBean(name = "publisherErrorsHandler")
    public Function<Message<?>, MessageHandler> publisherErrorsHandler() {
        return msg -> {
            log.info("Error while publishing message {}", msg);
            return null;
        };
    }
}

