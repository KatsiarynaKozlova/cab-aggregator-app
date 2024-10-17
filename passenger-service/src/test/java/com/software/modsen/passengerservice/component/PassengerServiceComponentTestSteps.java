package com.software.modsen.passengerservice.component;

import com.software.modsen.passengerservice.dto.request.PassengerForRating;
import com.software.modsen.passengerservice.kafka.producer.PassengerProducer;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.impl.DefaultPassengerService;
import com.software.modsen.passengerservice.util.PassengerTestUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class PassengerServiceComponentTestSteps {
    @InjectMocks
    private DefaultPassengerService passengerService;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private PassengerProducer passengerProducer;
    private Passenger passenger = new Passenger();

    @Given("Passenger request with name {string}, email {string}, phone {string}")
    public void passengerRequestWithNameEmailPhone(String name, String email, String phone) {
        passenger.setName(name);
        passenger.setEmail(email);
        passenger.setPhone(phone);
    }

    @When("Passenger is created")
    public void passengerIsCreated() {
        doAnswer(invocation -> {
            return null;
        }).when(passengerProducer).sendPassengerId(any(PassengerForRating.class));
        when(passengerRepository.save(any(Passenger.class)))
                .thenAnswer(invocation -> invocation.<PassengerRepository>getArgument(0));
        passenger = passengerService.createPassenger(passenger);
    }

    @Then("the Passenger should have email {string}, phone {string}")
    public void thePassengerShouldHaveEmailPhone(String email, String phone) {
        assertEquals(email, passenger.getEmail());
        assertEquals(phone, passenger.getPhone());
    }

    @Given("Existing Passenger with id {long}")
    public void existingPassengerWithId(Long passengerId) {
        passenger = new Passenger();
        passenger.setPassengerId(passengerId);
    }

    @When("update passenger with email {string}, phone {string}")
    public void updatePassengerWithEmailPhone(String email, String phone) {
        when(passengerRepository.findById(passenger.getPassengerId())).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(any(Passenger.class))).thenAnswer(invocation -> {
            Passenger savedPassenger = invocation.getArgument(0);
            savedPassenger.setEmail(email);
            savedPassenger.setPhone(phone);
            return savedPassenger;
        });

        passenger = passengerService.updatePassenger(1L, PassengerTestUtil.getDefaultPreUpdatePassenger());
    }

    @Then("The response should have passengerId {long}")
    public void theResponseShouldHavePassengerId(Long passengerId) {
        assertEquals(passengerId, passenger.getPassengerId());
    }

    @And("Response have email {string}, phone {string}")
    public void responseHaveEmailPhone(String email, String phone) {
        assertEquals(email, passenger.getEmail());
        assertEquals(phone, passenger.getPhone());
    }

    @Given("existing Passenger with id {long}, name {string}, email {string}, phone {string}")
    public void existingPassengerWithIdNameEmailPhone(Long passengerId, String name, String email, String phone) {
        passenger = new Passenger();
        passenger.setPassengerId(passengerId);
        passenger.setPhone(phone);
        passenger.setEmail(email);
        passenger.setName(name);
    }

    @When("the id {long} is passed to the findById method")
    public void theIdIsPassedToTheFindByIdMethod(Long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));
    }

    @Then("The response should contain passenger with id {long}")
    public void theResponseShouldContainPassengerWithId(Long passengerId) {
        assertEquals(passengerId, passenger.getPassengerId());
    }
}
