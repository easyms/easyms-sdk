package com.easyms.sampleapp.api;

import com.easyms.azure.test.AbstractResourceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



public class ClientResourceTest extends AbstractResourceTest {

    private static final String API_HR_ID = "/api/v1/clients/{id}";



    @Test
    @WithMockUser(username="jean.dupont@toto.com",authorities={"PERM_READ_CLIENT_NO"})
    public void should_return_forbidden_when_not_permitted() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1").with(bearerToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(username="jean.dupont@toto.com",authorities={"PERM_READ_CLIENT"})
    public void should_return_client_when_client_exists() throws Exception {
        mockMvc.perform(get(API_HR_ID, "1").with(bearerToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="jean.dupont@toto.com",authorities={"PERM_READ_CLIENT"})
    public void should_return_notfound_when_client_not_exists() throws Exception {
        mockMvc.perform(get(API_HR_ID, "2").with(bearerToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}