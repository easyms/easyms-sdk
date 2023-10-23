package com.easyms.sampleapp.api;

import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.dto.ClientRequest;
import com.easyms.sampleapp.service.ClientService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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


    @Operation(summary="returns all details of a client by feign")
    @Timed
    @PreAuthorize("hasAuthority('PERM_READ_CLIENT')")
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v2/clients/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
        log.info("get client with feign");
        Optional<ClientDto> clientDto = clientService.getById(id);
        return clientDto.map(clDto -> ResponseEntity.ok().body(clDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary="create new client by feign")
    @Timed
    @PreAuthorize("hasAuthority('PERM_UPDATE_CLIENT')")
    @PostMapping(produces = APPLICATION_JSON_VALUE, path = "/v2/clients")
    ResponseEntity<ClientDto> createClient(@RequestBody @Valid ClientRequest clientRequest){
        log.info("create new client with feign");
        ClientDto clientDto = clientService.create(clientRequest);
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/v1/clients/{id}").build().expand(clientDto.getId()).toUri();
        return ResponseEntity.created(location).body(clientDto);
    }

}

