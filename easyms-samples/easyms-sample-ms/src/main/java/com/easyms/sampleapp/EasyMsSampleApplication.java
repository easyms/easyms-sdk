package com.easyms.sampleapp;

import com.easyms.sampleapp.config.EasyMsSampleMsConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.SubscribableChannel;

@Slf4j
@SpringBootApplication
@Import(EasyMsSampleMsConfiguration.class)
@AllArgsConstructor
@EnableBinding(EasyMsSampleApplication.InputChannel.class)
public class EasyMsSampleApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EasyMsSampleApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }

    interface InputChannel {
        @Input("dummyQueueChannel")
        SubscribableChannel dummyMessageChannel();
    }

}
