package com.software.modsen.passengerservice.util;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.model.Passenger;
import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class PassengerTestUtil {
    public static final Long DEFAULT_PASSENGER_ID = 1L;
    public static final String DEFAULT_NAME = "George";
    public static final String DEFAULT_EMAIL = "dnsauto@mail.ru";
    public static final String DEFAULT_PHONE = "1234567890";
    public static final Boolean DEFAULT_IS_DELETED = Boolean.FALSE;
    public static final String NEW_EMAIL = "dhcpauto@gmail.com";
    public static final String NEW_PHONE = "0987654321";

    public Passenger getDefaultPassenger() {
        return new Passenger(
                DEFAULT_PASSENGER_ID,
                DEFAULT_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE,
                DEFAULT_IS_DELETED
        );
    }

    public Passenger getDefaultPreCreatePassenger() {
        return new Passenger(
                null,
                DEFAULT_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE,
                DEFAULT_IS_DELETED
        );
    }

    public PassengerRequest getDefaultPassengerRequest() {
        return new PassengerRequest(
                DEFAULT_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE
        );
    }

    public PassengerResponse getDefaultPassengerResponse() {
        return new PassengerResponse(
                DEFAULT_PASSENGER_ID,
                DEFAULT_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE
        );
    }

    public PassengerRequest getDefaultUpdatePassengerRequest() {
        return new PassengerRequest(
                DEFAULT_NAME,
                NEW_EMAIL,
                NEW_PHONE
        );
    }

    public Passenger getDefaultPreUpdatePassenger() {
        return new Passenger(
                null,
                DEFAULT_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE,
                DEFAULT_IS_DELETED
        );
    }

    public Passenger getDefaultUpdatedPassenger() {
        return new Passenger(
                DEFAULT_PASSENGER_ID,
                DEFAULT_NAME,
                NEW_EMAIL,
                NEW_PHONE,
                DEFAULT_IS_DELETED
        );
    }

    public PassengerResponse getDefaultUpdatedPassengerResponse() {
        return new PassengerResponse(
                DEFAULT_PASSENGER_ID,
                DEFAULT_NAME,
                NEW_EMAIL,
                NEW_PHONE
        );
    }
}
