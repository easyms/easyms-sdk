package com.easyms.messaging.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.function.Function;

@Configuration
public class PublisherConfirmsConfiguration {


    @Autowired
    @Qualifier("publisherConfirmsHandler")
    Function<Message<?>, MessageHandler> confirmHandler;


    @Autowired
    @Qualifier("publisherErrorsHandler")
    Function<Message<?>, MessageHandler> errorsHandler;

    @ServiceActivator(inputChannel = "acks")
    public void acks(Message<?> ack) {
        confirmHandler.apply(ack);
        System.out.println("Ack: " + ack);
    }

    @ServiceActivator(inputChannel = "easyms-exchange.errors")
    public void errors(Message<?> error) {
        errorsHandler.apply(error);
    }
}
