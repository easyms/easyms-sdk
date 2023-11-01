package com.easyms.sampleapp.client;

import com.easyms.sampleapp.model.dto.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "easyms-secured-azure-ad-sample-ms", url = "http://localhost:8092")
public interface FeignTestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/clients/{id}", produces = "application/json")
    ClientDto getClient(@PathVariable("id") Long id);

}
