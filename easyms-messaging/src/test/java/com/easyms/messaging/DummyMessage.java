package com.easyms.messaging;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DummyMessage {
    private String title;
    private String description;
    private String metadata;

}
