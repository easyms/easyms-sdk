package com.easyms.sampleapp.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRequest {

    private String firstname;
    private String lastname;
    private String email;
}
