package com.easyms.rest.ms.error;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorItemDto {

    private String field;
    private String key;
    private String message;

    public ErrorItemDto(String field, String key) {
        this.field = field;
        ErrorMessage message = ErrorRepository.getMessage(key);
        this.key = message.getErrorKey();
        this.message = message.getErrorMessage();
    }


}
