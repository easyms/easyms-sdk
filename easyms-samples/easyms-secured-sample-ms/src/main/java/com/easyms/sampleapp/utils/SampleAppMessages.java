package com.easyms.sampleapp.utils;

import com.easyms.rest.ms.error.CommonErrorMessages;
import com.easyms.rest.ms.error.ErrorMessage;
import com.easyms.rest.ms.error.ErrorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


@Component
public class SampleAppMessages extends CommonErrorMessages {

    public static final String EMAIL_USED = "email.used";
    public static final String INVALID_EMAIL = "invalid.email";

    /***/

    public static ErrorMessage email_used;
    public static ErrorMessage invalid_email;



    @PostConstruct
    public void load() {
        email_used = ErrorRepository.build(EMAIL_USED, "Email used.");
        invalid_email = ErrorRepository.build(INVALID_EMAIL, "Invalid email.");
    }

}

