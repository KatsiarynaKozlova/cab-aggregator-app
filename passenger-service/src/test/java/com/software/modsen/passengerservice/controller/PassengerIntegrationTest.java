package com.software.modsen.passengerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.config.DatabaseContainerConfiguration;
import com.software.modsen.passengerservice.config.KafkaContainerConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({DatabaseContainerConfiguration.class, KafkaContainerConfiguration.class})
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

    @Test
    public void testGetAllPassengers_ShouldReturnListOfPassengers() throws Exception {
        mockMvc.perform(get("/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    public void getTestUpdatePassenger_ShouldReturnUpdatedPassenger() throws Exception {
        PassengerRequest updatedPassenger = PassengerTestUtil.getDefaultPassengerRequest();
        Passenger expectedPassenger = PassengerTestUtil.getDefaultPassenger();
        mockMvc.perform(put("/passengers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPassenger)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengerId").value(expectedPassenger.getPassengerId()))
                .andExpect(jsonPath("$.name").value(expectedPassenger.getName()))
                .andExpect(jsonPath("$.email").value(expectedPassenger.getEmail()))
                .andExpect(jsonPath("$.phone").value(expectedPassenger.getPhone()));
    }

    @Test
    public void testUpdatePassenger_ShouldReturnNotFoundException() throws Exception {
        PassengerRequest updatedPassenger = PassengerTestUtil.getDefaultPassengerRequest();
        mockMvc.perform(put("/passengers/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPassenger)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateNewPassenger_ShouldReturnAlreadyExistException() throws Exception {
        PassengerRequest newPassengerRequest = PassengerTestUtil.getDefaultPassengerRequest();
        mockMvc.perform(post("/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassengerRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdatePassenger__ShouldReturnAlreadyExistException() throws Exception {
        PassengerRequest passengerRequest = PassengerTestUtil.getDefaultUpdatePassengerRequest();
        mockMvc.perform(post("/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerRequest)));

        mockMvc.perform(put("/passengers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isConflict());
    }
}
