package com.easyms.rest.ms.error;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CommonErrorMessages {

    public static final String METHOD_ARGUMENT_TYPE_MISMATCH = "method.argument.type.mismatch";
    public static final String MISSING_REQUIRED_PARAMETER = "missing.required.parameter";
    public static final String UNKNOWN_ERROR = "unknown.error";
    public static final String NUMERIC_OUT_OF_BOUND = "numeric.out.of.bound";
    public static final String STRING_OUT_OF_BOUND = "string.out.of.bound";
    public static final String ACCESS_DENIED = "access.denied";
    public static final String BAD_CREDENTIALS = "bad.credentials";



    // *********** Common error messages *********** //

    public static ErrorMessage unknown_error;
    public static ErrorMessage numeric_out_of_bound;
    public static ErrorMessage string_out_of_bound;
    public static ErrorMessage method_argument_type_mismatch;
    public static ErrorMessage missing_required_parameters;
    public static ErrorMessage bad_credentials;
    public static ErrorMessage access_denied;



    @PostConstruct
    public void load() {
        unknown_error = ErrorRepository.build(UNKNOWN_ERROR, "Unknown error.");
        numeric_out_of_bound = ErrorRepository.build(NUMERIC_OUT_OF_BOUND, "Numeric value out of bounds.");
        string_out_of_bound = ErrorRepository.build(STRING_OUT_OF_BOUND, "String length out of bounds.");
        method_argument_type_mismatch = ErrorRepository.build(METHOD_ARGUMENT_TYPE_MISMATCH, "Method argument type mismatch");
        missing_required_parameters = ErrorRepository.build(MISSING_REQUIRED_PARAMETER, "Missing required parameter.");
        access_denied =  ErrorRepository.build(ACCESS_DENIED, "Access Denied");
        bad_credentials = ErrorRepository.build(BAD_CREDENTIALS, "Bad credentials.");


    }

}
