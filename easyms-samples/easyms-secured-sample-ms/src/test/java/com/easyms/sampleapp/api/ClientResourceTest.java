package com.easyms.sampleapp.api;

import com.easyms.security.oauth2.ms.config.security.DefaultRoutesHandlerConfig;
import com.easyms.security.oauth2.ms.config.security.JwtAuthConverter;
import com.easyms.security.oauth2.ms.config.security.JwtAuthConverterProperties;
import com.easyms.security.oauth2.ms.config.security.WebSecurityConfiguration;
import com.easyms.test.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JwtAuthConverterProperties.class,
        JwtAuthConverter.class,
        WebSecurityConfiguration.class,
        //CORSFilter.class,
        DefaultRoutesHandlerConfig.class})
class ClientResourceTest extends AbstractTest {


    @Autowired
    private MockMvc mockMvc;
    private static final String API_HR_ID = "/api/v1/clients/{id}";


    @Test
    @WithMockUser(username = "jean.dupont@toto.com", authorities = {"user"})
    void should_return_forbidden_when_not_permitted() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "jean.dupont@toto.com", authorities = {"admin"})
    void should_return_client_when_client_exists() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "jean.dupont@toto.com", authorities = {"admin"})
    void should_return_notfound_when_client_not_exists() throws Exception {
        mockMvc.perform(get(API_HR_ID, "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
