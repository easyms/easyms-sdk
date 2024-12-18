package com.easyms.sampleapp.api;

import com.easyms.sampleapp.dto.Client;
import com.easyms.sampleapp.dto.DummyMessage;
import com.easyms.sampleapp.service.ClientService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping("/api")
@Validated
@Timed
public class ClientResource {

    private final ClientService clientService;
    private final StreamBridge streamBridge;


    public ClientResource(ClientService clientService, StreamBridge streamBridge) {
        this.clientService = clientService;
        this.streamBridge = streamBridge;
    }

    @Bean
    public Consumer<DummyMessage> onDummyReceive() {
        return dummy -> log.info("received message {} ", dummy);
    }

 /*   @Bean
    public Function<DummyMessage, DummyMessage> shareDummy() {
        return dummyMessage -> dummyMessage;
    }*/

/*    @Bean
    public Supplier<DummyMessage> sendDummy() {
        return () -> DummyMessage.builder()
                .title("this is a title")
                .description("this is the description")
                .metadata("this is metadata").build();
    }*/

    @Operation(summary = "returns all details of a client")
    @Timed
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients/{id}")
    public ResponseEntity<Client> findById(@PathVariable String id) {
        log.info("get client by id {}", id);
        Optional<Client> client = clientService.getById(id);

        DummyMessage dummyMessage = DummyMessage.builder()
                .title("this is a title")
                .description("this is the description")
                .metadata("this is metadata").build();

        streamBridge.send("sendDummy-out-0", dummyMessage);
        return client.map(clientDto -> ResponseEntity.ok().body(clientDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

