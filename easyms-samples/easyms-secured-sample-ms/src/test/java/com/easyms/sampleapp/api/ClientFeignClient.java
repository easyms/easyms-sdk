package com.easyms.sampleapp.api;



import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "client-service", url = "${client.service.url}", configuration = ClientFeignConfiguration.class)
@Hidden
public interface ClientFeignClient {

    //@PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    @RequestLine("POST /api/upload")
    @Headers("Content-Type: multipart/form-data")
    ResponseEntity uploadFiles(@Param("file") MultipartFile[] file);

}

