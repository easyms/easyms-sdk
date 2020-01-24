package com.easyms.sampleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ClientDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
