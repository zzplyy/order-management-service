package com.petproject.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.dto.ProductDTO;
import com.petproject.entity.Product;
import com.petproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndGetProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(BigDecimal.valueOf(99.99));

        String json = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Product"));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Сначала создаем продукт
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Initial Name");
        productDTO.setDescription("Initial Desc");
        productDTO.setPrice(BigDecimal.valueOf(10));

        Product saved = productRepository.save(
                new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice())
        );

        productDTO.setName("Updated Name");
        productDTO.setDescription("Updated Desc");
        productDTO.setPrice(BigDecimal.valueOf(20));

        String json = objectMapper.writeValueAsString(productDTO);

        // Обновляем
        mockMvc.perform(put("/products/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product("ToDelete", "ToDelete", BigDecimal.valueOf(1));
        Product saved = productRepository.save(product);

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}
