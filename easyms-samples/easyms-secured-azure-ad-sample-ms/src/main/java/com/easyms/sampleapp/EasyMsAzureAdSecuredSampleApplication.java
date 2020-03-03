package com.easyms.sampleapp;

import com.easyms.sampleapp.config.EasyMsSampleMsConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Import(EasyMsSampleMsConfiguration.class)
@AllArgsConstructor
public class EasyMsAzureAdSecuredSampleApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EasyMsAzureAdSecuredSampleApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }

}
