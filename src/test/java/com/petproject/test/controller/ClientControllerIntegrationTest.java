package com.petproject.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.entity.Client;
import com.petproject.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    void testCreateClient() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("+1234567890");

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Client")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.phone", is("+1234567890")));
    }

    @Test
    void testGetAllClients() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("+1234567890");
        clientRepository.save(client);

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Client")));
    }

    @Test
    void testGetClientById() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("+1234567890");
        Client savedClient = clientRepository.save(client);

        mockMvc.perform(get("/api/clients/" + savedClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedClient.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Client")));
    }

    @Test
    void testUpdateClient() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("+1234567890");
        Client savedClient = clientRepository.save(client);

        savedClient.setName("Updated Client");
        savedClient.setEmail("updated@example.com");

        mockMvc.perform(put("/api/clients/" + savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Client")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    void testDeleteClient() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("+1234567890");
        Client savedClient = clientRepository.save(client);

        mockMvc.perform(delete("/api/clients/" + savedClient.getId()))
                .andExpect(status().isNoContent());

        Optional<Client> deletedClient = clientRepository.findById(savedClient.getId());
        assert (deletedClient.isEmpty());
    }
}
