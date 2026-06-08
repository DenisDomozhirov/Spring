package com.example.SpringWebDemo.controller;

import com.example.SpringWebDemo.model.Pet;
import com.example.SpringWebDemo.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUser() throws Exception {

        var newUser = new User(null,
                "John",
                "qwerty@email.com",
                22,
                null
        );
        String userJson = objectMapper.writeValueAsString(newUser);
        String createdUserJson = mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User userResponse = objectMapper.readValue(createdUserJson, User.class);

        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(newUser.getName(), userResponse.getName());
    }

    @Test
    void shouldFindUserById() throws Exception {

        var newUser = new User(null,
                "John",
                "qwerty@email.com",
                22,
                null
        );
        String userJson = objectMapper.writeValueAsString(newUser);
        String createdUserJson = mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User userResponse = objectMapper.readValue(createdUserJson, User.class);
        Long userId = userResponse.getId();

        mockMvc.perform(get("/users/findById/{id}", userId)).andExpect(status().isOk());
    }
}