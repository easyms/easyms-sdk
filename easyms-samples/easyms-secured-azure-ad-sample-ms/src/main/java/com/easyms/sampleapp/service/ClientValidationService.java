package com.easyms.sampleapp.service;

import com.easyms.rest.ms.rest.Validator;
import com.easyms.sampleapp.model.dto.ClientRequest;
import com.easyms.sampleapp.model.entity.Client;
import com.easyms.sampleapp.repository.ClientRepository;
import com.easyms.sampleapp.utils.SampleAppMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
@Slf4j
public class ClientValidationService {

    private final ClientRepository clientRepository;

    public void validateCreateClient(ClientRequest clientRequest) {
        Optional<Client> client = clientRepository.findByEmail(clientRequest.getEmail());
        Validator.of(client)
                .validateIf(Optional::isPresent, () -> new IllegalStateException(SampleAppMessages.email_used.getErrorKey()))
                .execute();

    }

}
