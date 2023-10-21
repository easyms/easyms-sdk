package com.easyms.sampleapp.model.dto;

import com.easyms.sampleapp.utils.SampleAppMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

