// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.easyms.sampleapp.servicebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@Profile("manual")
public class ServiceProducerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumerConfiguration.class);

    @Autowired
    @Qualifier("emitter")
    private Sinks.Many<Message<String>> emitterProcessor;

    @Autowired
    @Qualifier("emitter2")
    private Sinks.Many<Message<String>> emitterProcessor2;

    @PostMapping("/api/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        LOGGER.info("Going to add message {} to emitter", message);
        emitterProcessor.emitNext(MessageBuilder.withPayload(message).build(), Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(3)));
        emitterProcessor2.emitNext(MessageBuilder.withPayload(message).build(), Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(3)));
        return ResponseEntity.ok("Sent!");
    }

    @GetMapping("/api/welcome")
    public String welcome() {
        return "welcome";
    }
}
