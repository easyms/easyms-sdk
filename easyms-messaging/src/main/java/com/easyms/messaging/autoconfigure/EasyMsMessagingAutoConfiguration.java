package com.easyms.messaging.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@Configuration
@ComponentScan
@EnableBinding(StandardChannels.class)
@Slf4j
public class EasyMsMessagingAutoConfiguration {


    @ConditionalOnMissingBean(name= "publisherConfirmsHandler")
    @Bean
    public MessageHandler publisherConfirmsHandler() {
        return msg -> log.info("acknowledged message {}", msg);
    }

    @ConditionalOnMissingBean(name= "publisherErrorsHandler")
    @Bean
    public MessageHandler publisherErrorsHandler() {
        return msg -> log.info("Error while publishing message {}", msg);
    }

}
