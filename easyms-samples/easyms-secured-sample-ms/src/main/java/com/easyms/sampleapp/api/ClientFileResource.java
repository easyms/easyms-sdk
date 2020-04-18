package com.easyms.sampleapp.api;


import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.StandardCopyOption;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequestMapping("/api")
@Validated
@AllArgsConstructor
@Timed
public class ClientFileResource {


    @ApiOperation("create new client file")
    @Timed
    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFiles(@RequestPart(name = "file") MultipartFile[] file) throws IOException {

        if(file.length > 0) {
            InputStream inputStream = file[0].getInputStream();

            File targetFile = new File("/Users/anis/dev/eff/toto.yml");

            OutputStream outStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outStream);
        }

        System.out.println("**** received uploaded file ****");
        return ResponseEntity.ok().build();
    }



}
