package com.easyms.sampleapp.converter;

import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.entity.Client;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;


@Data(staticConstructor = "newInstance")
public class ClientConverter implements Converter<Client, ClientDto> {

    @Override
    public ClientDto convert(Client client){
        if(Objects.isNull(client)){
            return null;
        }
        return ClientDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }
}
