package com.easyms.sampleapp.converter;

import com.easyms.sampleapp.model.dto.ClientRequest;
import com.easyms.sampleapp.model.entity.Client;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

@Data(staticConstructor = "newInstance")
public class ClientRequestConverter implements Converter<ClientRequest, Client> {

    @Override
    public Client convert(ClientRequest clientRequest){
        if (Objects.isNull(clientRequest)) {
            return null;
        }
        return Client.builder()
                .firstname(clientRequest.getFirstname())
                .lastname(clientRequest.getLastname())
                .email(clientRequest.getEmail())
                .build();

    }
}
