package com.easyms.sampleapp.utils;

import com.easyms.common.ms.error.CommonErrorMessages;
import com.easyms.common.ms.error.ErrorMessage;
import com.easyms.common.ms.error.ErrorRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SampleAppMessages extends CommonErrorMessages {

    public static final String EMAIL_USED = "email.used";
    public static final String INVALID_EMAIL = "invalid.email";

    /****************************************/

    public static ErrorMessage email_used;
    public static ErrorMessage invalid_email;



    @PostConstruct
    public void load() {
        email_used = ErrorRepository.build(EMAIL_USED, "Email used.");
        invalid_email = ErrorRepository.build(INVALID_EMAIL, "Invalid email.");
    }

}
