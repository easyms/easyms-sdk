package com.easyms.messaging.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHandler;

import java.util.function.Supplier;

@Configuration
@ComponentScan
@Slf4j
public class EasyMsMessagingAutoConfiguration {

    //TODO Fix Unit Tests
    @Bean
    @ConditionalOnMissingBean(name = "publisherConfirmsHandler")
    public Supplier<MessageHandler> publisherConfirmsHandler() {
        return this::getConfirmMessage;
    }

    private MessageHandler getConfirmMessage() {
        return msg -> log.info("acknowledged message {}", msg);
    }

    @Bean
    @ConditionalOnMissingBean(name = "publisherErrorsHandler")
    public Supplier<MessageHandler> publisherErrorsHandler() {
        return this::getErrorsMessage;
    }

    private MessageHandler getErrorsMessage() {
        return msg -> log.info("Error while publishing message {}", msg);
    }
}

