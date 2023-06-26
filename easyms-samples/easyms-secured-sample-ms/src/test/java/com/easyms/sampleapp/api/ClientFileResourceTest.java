package com.easyms.sampleapp.api;

import com.easyms.test.AbstractTest;
import jakarta.inject.Inject;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.io.OutputStream;


@EnableFeignClients
@Disabled
class ClientFileResourceTest extends AbstractTest {

    private static final String API_UPLOAD = "/api/upload";
    @Inject
    private ClientFeignClient clientFeignClient;

    @Inject
    ResourceLoader resourceLoader;


    @Test
    void should_send_multipartfile() throws Exception {

        Resource resource = resourceLoader.getResource("classpath:bootstrap.yml");

        DiskFileItem fileItem = new DiskFileItem("file", "text/plain", true, "fileName", 0,
                null);

        //DiskFileItem is based on a DeferredOutputstream
        try (OutputStream out = fileItem.getOutputStream();
             InputStream in = resource.getInputStream()) {
            IOUtils.copy(in, out);
        }

        MockMultipartFile multipartFile = new MockMultipartFile("bootstrap.yml",
                fileItem.getInputStream().readAllBytes());


        byte[] bytes = multipartFile.getBytes();

        clientFeignClient.uploadFiles(ArrayUtils.toArray(multipartFile));
        Thread.sleep(10000);
    }


}
