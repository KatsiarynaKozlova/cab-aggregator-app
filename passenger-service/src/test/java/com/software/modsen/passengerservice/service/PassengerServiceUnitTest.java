package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.kafka.producer.PassengerProducer;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.impl.DefaultPassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceUnitTest {
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private PassengerProducer passengerProducer;
    @InjectMocks
    private DefaultPassengerService passengerService;

    private final Passenger defaultMockPassenger =
            Passenger.builder().passengerId(1L).name("name").phone("1234567890").email("email@email.com").deleted(false).build();

    private final List<Passenger> defaultMockListPassengers = List.of(defaultMockPassenger);

    @ParameterizedTest
    @CsvSource("1")
    void getPassengerById_shouldReturnPassenger(Long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.of(defaultMockPassenger));

        Passenger passenger = passengerService.getPassengerById(id);

        assertNotNull(passenger);
        assertEquals(id, passenger.getPassengerId());
        verify(passengerRepository, times(1)).findById(id);
    }

    @ParameterizedTest
    @CsvSource("2")
    void getPassengerById_shouldThrowNotFoundException(Long id) {
        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerById(id));

        verify(passengerRepository, times(1)).findById(id);
    }

    @Test
    void getListOfPassengers_shouldReturnList() {
        when(passengerRepository.findAll()).thenReturn(defaultMockListPassengers);

        List<Passenger> passengerList = passengerService.getAllPassengers();

        assertNotNull(passengerList);
        assertEquals(1, passengerList.size());
        verify(passengerRepository, times(1)).findAll();
    }

    @Test
    void getListOfPassengers_shouldReturnEmptyList() {
        when(passengerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Passenger> passengerList = passengerService.getAllPassengers();

        assertNotNull(passengerList);
        assertTrue(passengerList.isEmpty());
        verify(passengerRepository, times(1)).findAll();
    }

    @Test
    void createPassenger_shouldReturnPassenger() {
        when(passengerRepository.save(any(Passenger.class))).thenReturn(defaultMockPassenger);

        Passenger newPassenger = passengerService.createPassenger(defaultMockPassenger);

        assertNotNull(newPassenger);
        assertEquals(newPassenger, defaultMockPassenger);
        verify(passengerRepository, times(1)).save(defaultMockPassenger);
    }

    @ParameterizedTest
    @CsvSource("email@email.com")
    void createPassenger_shouldThrowEmailAlreadyExistException(String email) {
        when(passengerRepository.existsByEmail(email)).thenThrow(EmailAlreadyExistException.class);
        assertThrows(
                EmailAlreadyExistException.class,
                () -> passengerService.createPassenger(defaultMockPassenger)
        );
        verify(passengerRepository).existsByEmail(email);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @ParameterizedTest
    @CsvSource("1234567890")
    void createPassenger_shouldThrowPhoneAlreadyExistException(String phone) {
        when(passengerRepository.existsByPhone(phone)).thenThrow(PhoneAlreadyExistException.class);
        assertThrows(
                PhoneAlreadyExistException.class,
                () -> passengerService.createPassenger(defaultMockPassenger)
        );
        verify(passengerRepository).existsByPhone(phone);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @ParameterizedTest
    @CsvSource("1")
    void updatePassengerById_shouldReturnPassenger(Long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.of(defaultMockPassenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(defaultMockPassenger);

        Passenger updatedPassenger = passengerService.updatePassenger(id, defaultMockPassenger);
        assertNotNull(updatedPassenger);
        assertEquals(updatedPassenger, defaultMockPassenger);

        verify(passengerRepository).findById(id);
        verify(passengerRepository).save(defaultMockPassenger);
    }

    @ParameterizedTest
    @CsvSource("2")
    void updatePassengerById_shouldThrowPassengerNorFoundException(Long id) {
        assertThrows(PassengerNotFoundException.class, () -> passengerService.updatePassenger(id, defaultMockPassenger));
        verify(passengerRepository, times(1)).findById(id);
    }

    @ParameterizedTest
    @CsvSource("1234567890")
    void updatePassenger_shouldThrowPhoneAlreadyExistException(String phone) {
        when(passengerRepository.existsByPhone(phone)).thenThrow(PhoneAlreadyExistException.class);
        assertThrows(
                PhoneAlreadyExistException.class,
                () -> passengerService.createPassenger(defaultMockPassenger)
        );
        verify(passengerRepository).existsByPhone(phone);
    }

    @ParameterizedTest
    @CsvSource("existingemail@email.com")
    void testUpdatePassenger_shouldThrowEmailAlreadyExists(String email) {
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setEmail(email);

        when(passengerRepository.findById(defaultMockPassenger.getPassengerId())).thenReturn(Optional.of(defaultMockPassenger));
        when(passengerRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(
                EmailAlreadyExistException.class,
                () -> passengerService.updatePassenger(defaultMockPassenger.getPassengerId(), updatedPassenger)
        );

        verify(passengerRepository, times(1)).findById(defaultMockPassenger.getPassengerId());
        verify(passengerRepository, times(1)).existsByEmail(email);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @ParameterizedTest
    @CsvSource("0987654321")
    void testUpdatePassenger_PhoneAlreadyExists(String phone) {
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setPhone(phone);
        updatedPassenger.setEmail(defaultMockPassenger.getEmail());

        when(passengerRepository.findById(defaultMockPassenger.getPassengerId())).thenReturn(Optional.of(defaultMockPassenger));
        when(passengerRepository.existsByPhone(phone)).thenReturn(true);

        assertThrows(
                PhoneAlreadyExistException.class,
                () -> passengerService.updatePassenger(defaultMockPassenger.getPassengerId(), updatedPassenger)
        );

        verify(passengerRepository, times(1)).findById(defaultMockPassenger.getPassengerId());
        verify(passengerRepository, times(1)).existsByPhone(phone);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void testDeletePassenger_Success() {
        Long passengerId = 1L;
        passengerService.deletePassenger(passengerId);
        verify(passengerRepository, times(1)).deleteById(passengerId);
    }

    @ParameterizedTest
    @CsvSource("1")
    void testUpdatePassenger_PassengerNotFound(Long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.updatePassenger(id, defaultMockPassenger)
        );
        verify(passengerRepository, times(1)).findById(id);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }
}
