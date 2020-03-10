package com.easyms.rest.ms.error;

import java.util.HashMap;
import java.util.Map;

public class ErrorRepository {

    private static final Map<String, ErrorMessage> errorStore = new HashMap<>();

    public static ErrorMessage build(String key, String description) {
        ErrorMessage message = new ErrorMessage(key, description);
        errorStore.put(key, message);
        return message;
    }

    public static ErrorMessage getOrDefault(String key, ErrorMessage defaultValue) {
        return errorStore.getOrDefault(key, defaultValue);
    }

    public static ErrorMessage getMessage(String key) {
        return errorStore.getOrDefault(key, CommonErrorMessages.unknown_error);
    }
}
