package com.easyms.messaging.autoconfigure;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StandardChannels {

    @Output("publishingChannel")
    MessageChannel publishingChannel();

    @Input("confirmChannel")
    SubscribableChannel confirmChannel();
}
