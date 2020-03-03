package com.easyms.sampleapp.service;

import com.easyms.sampleapp.converter.ClientConverter;
import com.easyms.sampleapp.converter.ClientRequestConverter;
import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.dto.ClientRequest;
import com.easyms.sampleapp.model.entity.Client;
import com.easyms.sampleapp.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Optional<ClientDto> getById(Long id) {
       Optional<Client> client = clientRepository.findById(id);
       return client.map(cl -> ClientConverter.newInstance().convert(cl));
    }

    public ClientDto create(ClientRequest clientRequest){
        Client client = ClientRequestConverter.newInstance().convert(clientRequest);
        Client c = clientRepository.save(client);
        return ClientConverter.newInstance().convert(c);
    }
    public List<ClientDto> getAll(){
        List<Client> clientDtos = clientRepository.findAll();
        return clientDtos.stream().map(client -> ClientConverter.newInstance().convert(client)).collect(Collectors.toList());
    }
    public ClientDto getByEmail(String email){
        Optional<Client> client = clientRepository.findByEmail(email);
        return ClientConverter.newInstance().convert(client.orElse(null));
    }
}
