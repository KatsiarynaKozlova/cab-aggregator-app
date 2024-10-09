package com.software.modsen.passengerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.config.DatabaseContainerConfiguration;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.util.PassengerTestUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.software.modsen.passengerservice.util.PassengerTestUtil.DEFAULT_PASSENGER_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(DatabaseContainerConfiguration.class)
public class PassengerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testGetByIdPassenger_ShouldReturnNotFoundException() throws Exception {
        mockMvc.perform(get("/passengers/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    public void testCreatePassenger_ShouldReturnNewPassenger() throws Exception {
        PassengerRequest newPassengerRequest = PassengerTestUtil.getDefaultPassengerRequest();
        mockMvc.perform(post("/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassengerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.passengerId").value(DEFAULT_PASSENGER_ID));
    }

    @Test
    @Order(3)
    public void testGetByIdPassenger_ShouldReturnPassenger() throws Exception {
        Passenger expectedPassenger = PassengerTestUtil.getDefaultPassenger();
        mockMvc.perform(get("/passengers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengerId").value(expectedPassenger.getPassengerId()))
                .andExpect(jsonPath("$.name").value(expectedPassenger.getName()))
                .andExpect(jsonPath("$.email").value(expectedPassenger.getEmail()))
                .andExpect(jsonPath("$.phone").value(expectedPassenger.getPhone()));
    }


}
