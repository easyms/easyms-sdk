// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.easyms.sampleapp.servicebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
@Profile("manual")
public class ManualServiceProducerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumerConfiguration.class);

    @Bean
    public Sinks.Many<Message<String>> emitter() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Sinks.Many<Message<String>> emitter2() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<String>>> supply(@Qualifier("emitter") Sinks.Many<Message<String>> emitter) {
        return () -> emitter.asFlux()
                .doOnNext(m -> LOGGER.info("Manually sending message from emitter 1 {}", m))
                .doOnError(t -> LOGGER.error("Error encountered", t));
    }

    @Bean
    public Supplier<Flux<Message<String>>> supply2(@Qualifier("emitter2") Sinks.Many<Message<String>> emitter) {
        return () -> emitter.asFlux()
                .doOnNext(m -> LOGGER.info("Manually sending message from emitter 2 {}", m))
                .doOnError(t -> LOGGER.error("Error encountered", t));
    }
}
