package com.easyms.common.ms.error;

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
