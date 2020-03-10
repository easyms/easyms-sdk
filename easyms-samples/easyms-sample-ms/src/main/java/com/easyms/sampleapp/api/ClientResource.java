package com.easyms.sampleapp.api;

import com.easyms.sampleapp.dto.Client;
import com.easyms.sampleapp.service.ClientService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping("/api")
@Validated
@AllArgsConstructor
@Timed
public class ClientResource {

    private final ClientService clientService;

    @ApiOperation("returns all details of a client")
    @Timed
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients/{id}")
    public ResponseEntity<Client> findById(@PathVariable String id) {
        log.info("get client by id {}", id);
        Optional<Client> client = clientService.getById(id);
        return client.map(clientDto -> ResponseEntity.ok().body(clientDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
