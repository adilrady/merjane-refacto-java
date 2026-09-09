package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

// import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

// Specify the controller class you want to test
// This indicates to spring boot to only load UsersController into the context
// Which allows a better performance and needs to do less mocks
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private NotificationService notificationService;

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ProductRepository productRepository;

        @Test
        public void processOrderShouldReturnOkAndPersistOrder() throws Exception {
            List<Product> savedProducts = productRepository.saveAll(createProducts());
            // Given
            Order order = new Order();
            order.setItems(Set.copyOf(savedProducts));
            Order savedOrder = orderRepository.save(order);

            // When
            mockMvc.perform(post("/orders/{orderId}/processOrder", savedOrder.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            // Then
            var resultOrderOpt = orderRepository.findById(savedOrder.getId());
            assertTrue("Order should exist in the database", resultOrderOpt.isPresent());
            assertEquals(savedOrder.getId(), resultOrderOpt.get().getId());
        }

        private static List<Product> createProducts() {
            return List.of(
                    new Product(null, 15, 30, "NORMAL", "USB Cable", null, null, null),
                    new Product(null, 10, 0, "NORMAL", "USB Dongle", null, null, null),
                    new Product(null, 15, 30, "EXPIRABLE", "Butter", LocalDate.now().plusDays(26), null, null),
                    new Product(null, 90, 6, "EXPIRABLE", "Milk", LocalDate.now().minusDays(2), null, null),
                    new Product(null, 15, 30, "SEASONAL", "Watermelon", null, LocalDate.now().minusDays(2), LocalDate.now().plusDays(58)),
                    new Product(null, 15, 30, "SEASONAL", "Grapes", null, LocalDate.now().plusDays(180), LocalDate.now().plusDays(240))
            );
        }
}
