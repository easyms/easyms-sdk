package com.easyms.messaging;


import com.easyms.messaging.autoconfigure.EasyMsMessagingAutoConfiguration;
import com.easyms.messaging.autoconfigure.EasyMsMessagingEnvProcessor;
import com.easyms.messaging.autoconfigure.StandardChannels;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.cloud.stream.test.binder.TestSupportBinder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(classes = {EasyMsMessagingAutoConfiguration.class, EasyMsMessagingEnvProcessor.class})
@ComponentScan
@ActiveProfiles("rabbit")
public class EasymsMessagingTest {

    @Autowired
    private StandardChannels standardChannels;

    @Autowired
    @Qualifier("publishingChannel")
    private SubscribableChannel receivingChannel;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void should_send_message() throws IOException {
        AtomicReference<Message> receivedMessage = new AtomicReference<>();
        receivingChannel.subscribe(receivedMessage::set);

        DummyMessage dummyMessage = DummyMessage.builder()
                .title("this is a title")
                .description("this is the description")
                .metadata("this is metadata").build();

        //we send a message with the correct routing herder that will be used for inputChannel binding.
        standardChannels.publishingChannel().send(MessageBuilder.withPayload(dummyMessage)
                .setHeader("routeTo", "dummy.channel.queue")
                .build());


        //Check that the message was received by the sending channel
        Message gotMessage = receivedMessage.get();
        assertThat(gotMessage).isNotNull();
        assertThat(objectMapper.readTree(objectMapper.writeValueAsString(dummyMessage)))
                .isEqualTo(objectMapper.readTree(new String((byte[]) gotMessage.getPayload())));

        //TODO : test that message have been received by DummyInputChannel.

    }


    @SpringBootApplication
    @EnableBinding(MySampleMessagingApplication.DummyReceiverConfiguration.class)
    public static class MySampleMessagingApplication {
        public static void main(String[]args) {
            SpringApplication.run(MySampleMessagingApplication.class, args);
        }

        interface DummyReceiverConfiguration {
            @Input("dummyQueueChannel")
            SubscribableChannel dummyMessageChannel();
        }
    }


}
