package com.easyms.sampleapp.api;

import com.easyms.test.AbstractResourceTest;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;

import java.io.InputStream;
import java.io.OutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Disabled
@WithMockUser(username="jean.dupont@titi.com",authorities={"ROLE_ADMIN_CLIENT5"})
@EnableFeignClients
public class ClientFileResourceTest extends AbstractResourceTest {

    private static final String API_UPLOAD = "/api/upload";
    @Inject
    private ClientFeignClient clientFeignClient;

    @Inject
    ResourceLoader resourceLoader;


    @Test
    @WithMockUser(username="jean.dupont@toto.com")
    public void should_send_multipartfile() throws Exception {

        Resource resource = resourceLoader.getResource("classpath:application.yml");

        DiskFileItem fileItem = new DiskFileItem("file", "text/plain", true, "fileName", 0,
                null);

        //DiskFileItem is based on a DeferredOutputstream
        try (OutputStream out = fileItem.getOutputStream();
             InputStream in = resource.getInputStream()) {
            IOUtils.copy(in, out);
        }

        MultipartFile multipartFile = new CommonsMultipartFile(
                fileItem);



        byte[] bytes = multipartFile.getBytes();

        clientFeignClient.uploadFiles(ArrayUtils.toArray(multipartFile));
        Thread.sleep(10000);
    }


}