package com.easyms.sampleapp.service;

import com.easyms.sampleapp.dto.Client;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ClientService {

    public Optional<Client> getById(String id) {
        return Optional.of(id).filter((i) -> i.equals("1")).map((i) -> new Client("Eric", "Dupont", "eric.dupont@toto.com"));
    }
}
