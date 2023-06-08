package com.easyms.messaging;


import com.easyms.messaging.autoconfigure.EasyMsMessagingAutoConfiguration;
import com.easyms.messaging.autoconfigure.EasyMsMessagingEnvProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("rabbit")
@Slf4j
class EasymsMessagingTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_send_message() throws JsonProcessingException {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(CustomConfiguration.class, EasyMsMessagingAutoConfiguration.class, EasyMsMessagingEnvProcessor.class))
                .web(WebApplicationType.NONE).run("--spring.jmx.enabled=false")) {

            StreamBridge streamBridge = context.getBean(StreamBridge.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            DummyMessage dummyMessage = DummyMessage.builder()
                    .title("this is a title")
                    .description("this is the description")
                    .metadata("this is metadata").build();

            streamBridge.send("dummy-topic", MessageBuilder.withPayload(dummyMessage).build());

            var messageReceived = outputDestination.receive(1000, "dummy-topic");
            assertThat(messageReceived).isNotNull();
            assertThat(objectMapper.readTree(objectMapper.writeValueAsString(dummyMessage)))
                    .isEqualTo(objectMapper.readTree(new String((byte[]) messageReceived.getPayload())));

        }
    }

    @EnableAutoConfiguration
    public static class CustomConfiguration {
    }
}
