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
import reactor.core.publisher.EmitterProcessor;

@RestController
@Profile("manual")
public class ServiceProducerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumerConfiguration.class);

    @Autowired
    @Qualifier("emitter")
    private EmitterProcessor<Message<String>> emitterProcessor;

    @Autowired
    @Qualifier("emitter2")
    private EmitterProcessor<Message<String>> emitterProcessor2;

    @PostMapping("/api/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        LOGGER.info("Going to add message {} to emitter", message);
        emitterProcessor.onNext(MessageBuilder.withPayload(message).build());
        emitterProcessor2.onNext(MessageBuilder.withPayload(message).build());
        return ResponseEntity.ok("Sent!");
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
}
