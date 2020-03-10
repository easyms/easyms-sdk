package com.easyms.sampleapp.model.dto;

import com.easyms.sampleapp.utils.SampleAppMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientRequest {

    private String firstname;
    private String lastname;
    @NotBlank(message = SampleAppMessages.MISSING_REQUIRED_PARAMETER)
    @Email(message = SampleAppMessages.INVALID_EMAIL)
    private String email;
}
