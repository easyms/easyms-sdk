// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus.topic.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@Profile("manual")
public class ManualServiceProducerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusTopicBinderApplication.class);

    @Bean
    public EmitterProcessor<Message<String>> emitter() {
        return EmitterProcessor.create();
    }

    @Bean
    public EmitterProcessor<Message<String>> emitter2() {
        return EmitterProcessor.create();
    }

    @Bean
    public Supplier<Flux<Message<String>>> supply(@Qualifier("emitter") EmitterProcessor<Message<String>> emitter) {
        return () -> Flux.from(emitter)
                         .doOnNext(m -> LOGGER.info("Manually sending message {}", m))
                         .doOnError(t -> LOGGER.error("Error encountered", t));
    }

    @Bean
    public Supplier<Flux<Message<String>>> supply2(@Qualifier("emitter2") EmitterProcessor<Message<String>> emitter) {
        return () -> Flux.from(emitter)
            .doOnNext(m -> LOGGER.info("Manually sending message from emitter 2 {}", m))
            .doOnError(t -> LOGGER.error("Error encountered", t));
    }
}
