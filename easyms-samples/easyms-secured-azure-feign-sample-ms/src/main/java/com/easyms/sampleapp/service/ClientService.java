package com.easyms.sampleapp.service;

import com.easyms.sampleapp.client.TestClient;
import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.dto.ClientRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.logstash.logback.marker.ObjectFieldsAppendingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ClientService {

    private static Logger customLogger = LoggerFactory.getLogger("custom-business-events");
    private final TestClient testClient;

    public Optional<ClientDto> getById(Long id) {
        ClientDto client = testClient.getClient(id);

        //Log business event
        ClientLogging clientLogging = ClientLogging.builder().clientId(id.toString()).eventName("RequestById").clientRate(10).build();
        customLogger.info(new ObjectFieldsAppendingMarker(clientLogging), "clientEvent");

        return Optional.ofNullable(client);
    }

    public ClientDto create(ClientRequest clientRequest) {
        return testClient.postClient(clientRequest);
    }

    @Data
    @Builder
    private static class ClientLogging {
        private String clientId;
        private String eventName;
        private int clientRate;
    }
}
