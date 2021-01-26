package com.easyms.messaging.autoconfigure;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableBinding(StandardChannels.class)
public class EasyMsMessagingAutoConfiguration {
}
