package com.easyms.sampleapp.service;

import com.easyms.sampleapp.converter.ClientConverter;
import com.easyms.sampleapp.converter.ClientRequestConverter;
import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.dto.ClientRequest;
import com.easyms.sampleapp.model.entity.Client;
import com.easyms.sampleapp.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.logstash.logback.marker.ObjectFieldsAppendingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientService {

    private static Logger customLogger = LoggerFactory.getLogger("custom-business-events");
    private final ClientRepository clientRepository;

    public Optional<ClientDto> getById(Long id) {
        Optional<Client> client = clientRepository.findById(id);

        //Log business event
        ClientLogging clientLogging = ClientLogging.builder().clientId(id.toString()).eventName("RequestById").clientRate(10).build();
        customLogger.info(new ObjectFieldsAppendingMarker(clientLogging), "clientEvent");

        return client.map(cl -> ClientConverter.newInstance().convert(cl));
    }

    public ClientDto create(ClientRequest clientRequest) {
        Client client = ClientRequestConverter.newInstance().convert(clientRequest);
        Client c = clientRepository.save(client);
        return ClientConverter.newInstance().convert(c);
    }

    public List<ClientDto> getAll() {
        List<Client> clientDtos = clientRepository.findAll();
        return clientDtos.stream().map(client -> ClientConverter.newInstance().convert(client)).collect(Collectors.toList());
    }

    public ClientDto getByEmail(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        return ClientConverter.newInstance().convert(client.orElse(null));
    }

    @Data
    @Builder
    private static class ClientLogging {
        private String clientId;
        private String eventName;
        private int clientRate;
    }
}
