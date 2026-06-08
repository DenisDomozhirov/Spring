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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreatePet() throws Exception {

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


        var pet = new Pet(
                null,
                "Name1",
                userId
        );

        String petJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(post("/pets/createPet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet petResponse = objectMapper.readValue(createdPetJson, Pet.class);

        Assertions.assertNotNull(petResponse.getId());
        Assertions.assertEquals(pet.getName(), petResponse.getName());
    }



    @Test
    void shouldDeleteSomePet() throws Exception {

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

        var pet = new Pet(
                null,
                "Name1",
                userId
        );

        String petJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(post("/pets/createPet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet petResponse = objectMapper.readValue(createdPetJson, Pet.class);

        Long petId = petResponse.getId();

        mockMvc.perform(delete("/pets/{id}", petId)).andExpect(status().isNoContent());

    }
}