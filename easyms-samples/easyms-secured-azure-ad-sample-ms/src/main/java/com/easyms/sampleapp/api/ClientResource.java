package com.easyms.sampleapp.api;

import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.dto.ClientRequest;
import com.easyms.sampleapp.service.ClientService;
import com.easyms.sampleapp.service.ClientValidationService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
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
    private final ClientValidationService clientValidationService;

    @PostConstruct
    public void init() {
        System.out.println("toto");
    }

    @ApiOperation("returns all details of a client")
    @Timed
    @PreAuthorize("hasAuthority('PERM_READ_CLIENT')")
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
        Optional<ClientDto> clientDto = clientService.getById(id);
        return clientDto.map(clDto -> ResponseEntity.ok().body(clDto)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @ApiOperation("returns all details of a client by email")
    @Timed
    @PreAuthorize("hasAuthority('PERM_READ_CLIENT')")
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients/by-email")
    public ResponseEntity<ClientDto> findByEmail(@RequestParam String email){
        ClientDto clientDto = clientService.getByEmail(email);
        return ResponseEntity.ok().body(clientDto);
    }


    @ApiOperation("create new client")
    @Timed
    @PreAuthorize("hasAuthority('PERM_MODIFY_CLIENT')")
    @PostMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients")
    ResponseEntity<ClientDto> createClient(@RequestBody @Valid ClientRequest clientRequest){
        log.info("create new client");
        clientValidationService.validateCreateClient(clientRequest);
        ClientDto clientDto = clientService.create(clientRequest);
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/v1/clients/{id}").build().expand(clientDto.getId()).toUri();
        return ResponseEntity.created(location).body(clientDto);
    }

    @ApiOperation("search all clients")
    @Timed
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients")
    ResponseEntity<List<ClientDto>> getAllClients(){

        List<ClientDto> clientDtos = clientService.getAll();
        return ResponseEntity.ok().body(clientDtos);
    }


}
