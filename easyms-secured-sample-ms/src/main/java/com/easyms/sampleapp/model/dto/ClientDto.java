package com.easyms.sampleapp.model.dto;

import com.easyms.sampleapp.utils.SampleAppMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class ClientDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
