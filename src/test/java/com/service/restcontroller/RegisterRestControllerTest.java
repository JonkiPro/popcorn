package com.service.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.service.app.rest.request.RegisterDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterRestControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();
    }

    @Test
    public void testBadRegistration() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("JonkiPro");
        registerDTO.setEmail("jonkipro@email.com");
        registerDTO.setPassword("password1");
        registerDTO.setPasswordAgain("password1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(registerDTO);

        mockMvc
                .perform(post("/api/v1.0/register")
                             .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                             .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNotFoundToken() throws Exception {
        mockMvc
                .perform(put("/api/v1.0/register/token/{token}", "someToken"))
                .andExpect(status().isNotFound());
    }
}
