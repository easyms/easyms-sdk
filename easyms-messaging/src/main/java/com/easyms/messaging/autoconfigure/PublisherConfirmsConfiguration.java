package com.easyms.messaging.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@Configuration
public class PublisherConfirmsConfiguration {


    @Autowired
    @Qualifier("publisherConfirmsHandler")
    MessageHandler confirmHandler;


    @Autowired
    @Qualifier("publisherErrorsHandler")
    MessageHandler errorsHandler;

    @ServiceActivator(inputChannel = "acks")
    public void acks(Message<?> ack) {
        confirmHandler.handleMessage(ack);
        System.out.println("Ack: " + ack);
    }

    @ServiceActivator(inputChannel = "easyms-exchange.errors")
    public void errors(Message<?> error) {
        errorsHandler.handleMessage(error);
    }
}
