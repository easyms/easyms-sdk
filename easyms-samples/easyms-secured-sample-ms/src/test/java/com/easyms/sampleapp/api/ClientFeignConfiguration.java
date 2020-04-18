package com.easyms.sampleapp.api;

import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;

public class ClientFeignConfiguration {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";


    @Bean
    public Encoder encoder() {
        return new SpringFormEncoder();
    }

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    private String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbi1jbGllbnRAeW9wbWFpbC5jb20iLCJzY29wZSI6WyJFYXN5bXMiXSwiZXhwIjoxNTg3MjQ3OTEzLCJwZXJpbWV0ZXJzIjpbIjEiXSwiYXV0aG9yaXRpZXMiOlsiUEVSTV9HRVRfQUNDT1VOVCIsIlBFUk1fTU9ESUZZX0FDQ09VTlQiLCJST0xFX0FETUlOX0NMSUVOVCIsIlBFUk1fTU9ESUZZX0NMSUVOVCIsIlBFUk1fR0VUX0NMSUVOVCIsIlBFUk1fVVBEQVRFX1BBU1NXT1JEIl0sImp0aSI6Ijg4ODMyMzgxLTZjYzQtNGY5NC1iOGI5LTViZTljYWQ5NGIwNiIsImNsaWVudF9pZCI6InBsYXRmb3JtLXVpIn0.DnApTFhJmeAWVkz_ONj1YdhTB-0zpRRAxoogBcpAVswn-JAkQN6Q4S7SBm-gU3GIdMwjv8szaFX7BYJf9mRxKbpR3kWkR3PGOToWpPR24G4Fdwjh6lR33fG2i4otYwqOoq_6WduAbzSnm3LTxJhh-19czfXpUryvDhQRXR9gV5GhKr53Z67w2rF-uZIVAtxia_TxdHsUhXf8Wmi-3wR-equRnDg9CuH3_VYYQN4lj9-42dk9wNaQxz4uhtGnzLx8j_zJ_P36gaVKnhvrqle0Uj6E34GM3SSJoVycnX8kLBneJUfL94izryHmDFvucZ__Eno6ORtQZgA29-d-0sU-cw";
    //Needed for test purpose, replace with a valid token before testing
    @Bean
    public RequestInterceptor FeignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
            }
        };
    }


}
