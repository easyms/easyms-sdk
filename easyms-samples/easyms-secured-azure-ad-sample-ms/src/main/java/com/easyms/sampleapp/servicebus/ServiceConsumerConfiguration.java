// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.easyms.sampleapp.servicebus;

import com.azure.spring.messaging.checkpoint.Checkpointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

import static com.azure.spring.messaging.AzureHeaders.CHECKPOINTER;

/**
 * @author Warren Zhu
 */
@Configuration
public class ServiceConsumerConfiguration {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumerConfiguration.class);

    @Bean
    public Consumer<Message<String>> consume() {
        return message -> {
            Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            LOGGER.info("New message received consume:  '{}'", message);
            checkpointer.success()
                    .doOnSuccess(s -> LOGGER.info("Message '{}' successfully checkpointed", message.getPayload()))
                    .doOnError(e -> LOGGER.error("Error found ", e))
                    .block();
        };
    }


    @Bean
    public Consumer<Message<String>> consumeNew() {
        return message -> {
            Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            LOGGER.info("New message received from new sub consumeNew : '{}'", message);
            checkpointer.success()
                    .doOnSuccess(s -> LOGGER.info("Message '{}' successfully checkpointed", message.getPayload()))
                    .doOnError(e -> LOGGER.error("Error found", e))
                    .block();
        };
    }
}

