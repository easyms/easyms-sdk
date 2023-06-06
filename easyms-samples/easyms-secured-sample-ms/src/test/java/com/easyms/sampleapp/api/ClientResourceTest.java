package com.easyms.sampleapp.api;
/*
import com.easyms.test.AbstractResourceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser(username = "jean.dupont@titi.com", authorities = {"ROLE_ADMIN_CLIENT5"})
class ClientResourceTest extends AbstractResourceTest {

    private static final String API_HR_ID = "/api/v1/clients/{id}";


    @Test
    @WithMockUser(username = "jean.dupont@toto.com", authorities = {"ROLE_ADMIN_CLIENT2"})
    void should_return_forbidden_when_not_permitted() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "jean.dupont@toto.com", authorities = {"ROLE_ADMIN_CLIENT"})
    void should_return_client_when_client_exists() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "jean.dupont@toto.com", authorities = {"ROLE_ADMIN_CLIENT"})
    void should_return_notfound_when_client_not_exists() throws Exception {
        mockMvc.perform(get(API_HR_ID, "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}*/
