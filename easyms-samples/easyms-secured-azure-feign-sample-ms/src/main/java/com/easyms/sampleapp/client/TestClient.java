package com.easyms.sampleapp.client;

import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.dto.ClientRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "easyms-secured-azure-ad-sample-ms", url = "http://localhost:8092")
public interface TestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/clients/{id}", produces = "application/json")
    ClientDto getClient(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/clients", produces = "application/json", consumes = "application/json")
    ClientDto postClient(@RequestBody ClientRequest clientRequest);

}
