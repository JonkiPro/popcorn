package com.service.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.service.app.rest.request.SendMessageDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageRestControllerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final String USERNAME = "JonkiPro";
    private final String PASSWORD = "password1";
    private final String ROLE = "USER";

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();
    }

    @Test
    public void testSendMessage() throws Exception {
        SendMessageDTO message = new SendMessageDTO();
        message.setTo("JonkiPro2");
        message.setSubject("Hello");
        message.setText("World!");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(message);

        mockMvc
                .perform(post("/api/v1.0/messages/")
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testBadSendMessage() throws Exception {
        SendMessageDTO message = new SendMessageDTO();
        message.setTo("JonkiPro2");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(message);

        mockMvc
                .perform(post("/api/v1.0/messages/")
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetSentMessage() throws Exception {
        mockMvc
                .perform(get("/api/v1.0/messages/sent/{id}", 1)
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void testNotFoundSentMessage() throws Exception {
        mockMvc
                .perform(get("/api/v1.0/messages/sent/{id}", 2)
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetReceivedMessage() throws Exception {
        mockMvc
                .perform(get("/api/v1.0/messages/received/{id}", 2)
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void testNotFoundReceivedMessage() throws Exception {
        mockMvc
                .perform(get("/api/v1.0/messages/received/{id}", 1)
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSentMessagesIsZero() throws Exception {
        mockMvc
                .perform(get("/api/v1.0/messages/sent")
                            .param("q", "qwerty")
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetReceivedMessagesIsZero() throws Exception {
        mockMvc
                .perform(get("/api/v1.0/messages/received")
                            .param("q", "qwerty")
                        .with(user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}
