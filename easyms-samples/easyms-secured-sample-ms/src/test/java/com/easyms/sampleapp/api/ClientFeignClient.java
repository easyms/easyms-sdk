package com.easyms.sampleapp.api;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(name = "client-service", url = "${client.service.url}", configuration = ClientFeignConfiguration.class)
//@RequestMapping("/api")
@ApiIgnore
public interface ClientFeignClient {

    //@PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    @RequestLine("POST /api/upload")
    @Headers("Content-Type: multipart/form-data")
    ResponseEntity uploadFiles(@Param("file") MultipartFile[] file);

}
