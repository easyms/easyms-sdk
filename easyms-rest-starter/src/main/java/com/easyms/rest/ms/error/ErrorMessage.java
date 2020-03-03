package com.easyms.rest.ms.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private String errorKey;
    private String errorMessage;

}
