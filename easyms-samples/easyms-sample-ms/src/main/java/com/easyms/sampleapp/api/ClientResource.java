package com.easyms.sampleapp.api;

import com.easyms.messaging.autoconfigure.StandardChannels;
import com.easyms.sampleapp.dto.Client;
import com.easyms.sampleapp.dto.DummyMessage;
import com.easyms.sampleapp.service.ClientService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping("/api")
@Validated
@NoArgsConstructor
@Timed
public class ClientResource {

    @Inject
    private ClientService clientService;
    @Inject
    private StandardChannels standardChannels;

    @Autowired
    @Qualifier("dummyQueueChannel")
    private SubscribableChannel receivingChannel;

    @Inject
    private ObjectMapper objectMapper;

    @PostConstruct
    private void initSucbscriber() {
        receivingChannel.subscribe(msg -> {
            try {

                JsonNode message     = objectMapper.readTree(new String((byte[]) msg.getPayload()));
                log.info("received message " + objectMapper.writeValueAsString(message));

            } catch (IOException e) {
                throw new RuntimeException("Exception while reading rabbit message", e);
            }
        });
    }

    @ApiOperation("returns all details of a client")
    @Timed
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients/{id}")
    public ResponseEntity<Client> findById(@PathVariable String id)  {
        log.info("get client by id {}", id);
        Optional<Client> client = clientService.getById(id);

        /*DummyMessage dummyMessage = DummyMessage.builder()
                .title("this is a title")
                .description("this is the description")
                .metadata("this is metadata").build();

        //we send a message with the correct routing herder that will be used for inputChannel binding.
        sendMessage(dummyMessage);
         */
        return client.map(clientDto -> ResponseEntity.ok().body(clientDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void sendMessage(DummyMessage dummyMessage)  {
        standardChannels.publishingChannel().send(MessageBuilder.withPayload(dummyMessage)
                .setHeader("routeTo", "dummy.channel.queue")
                .build());
    }

}
