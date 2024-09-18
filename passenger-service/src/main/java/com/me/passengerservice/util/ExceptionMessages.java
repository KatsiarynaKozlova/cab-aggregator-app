package com.me.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String PASSENGER_NOT_FOUND_EXCEPTION = "passenger with id '%s' not found";
    public static final String PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION =
            "passenger with email '%s' already exists";
    public static final String PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION =
            "passenger with phone '%s' already exists";
}
