package com.petproject.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.entity.Client;
import com.petproject.entity.Order;
import com.petproject.model.OrderState;
import com.petproject.repository.ClientRepository;
import com.petproject.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        clientRepository.deleteAll();

        testClient = new Client();
        testClient.setName("Test Client");
        testClient.setEmail("test@example.com");
        testClient.setPhone("1234567890");
        testClient = clientRepository.save(testClient);
    }

    @Test
    void testCreateAndGetOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .param("clientId", String.valueOf(testClient.getId()))
                        .param("orderStatus", "NEW"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus", is("NEW")));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order(OrderState.NEW, testClient);
        order = orderRepository.save(order);

        mockMvc.perform(get("/api/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is("NEW")));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        Order order = new Order(OrderState.NEW, testClient);
        order = orderRepository.save(order);

        mockMvc.perform(put("/api/orders/" + order.getId())
                        .param("newStatus", "PROCESSING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", is("PROCESSING")));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Order order = new Order(OrderState.NEW, testClient);
        order = orderRepository.save(order);

        mockMvc.perform(delete("/api/orders/" + order.getId()))
                .andExpect(status().isNoContent());

        Optional<Order> deleted = orderRepository.findById(order.getId());
        assert (deleted.isEmpty());
    }
}
