// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.easyms.sampleapp.servicebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * @author Warren Zhu
 */
@Configuration
public class ServiceConsumerConfiguration {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumerConfiguration.class);

    @Bean
    public Consumer<Message<String>> consume() {
        return message -> {
            //Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            LOGGER.info("New message received: '{}'", message);
//            checkpointer.success().handle((r, ex) -> {
//                if (ex == null) {
//                    LOGGER.info("Message '{}' successfully checkpointed", message);
//                }
//                return null;
//            });
        };
    }



    @Bean
    public Consumer<Message<String>> consumeNew() {
        return message -> {
            //Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            LOGGER.info("New message received from new sub: '{}'", message);
//            checkpointer.success().handle((r, ex) -> {
//                if (ex == null) {
//                    LOGGER.info("Message '{}' successfully checkpointed", message);
//                }
//                return null;
//            });
        };
    }
}

