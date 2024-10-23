package com.software.modsen.passengerservice.util;

public final class LogInfoMessages {
    public static final String GET_PASSENGER = "get passenger with id '%s'";
    public static final String GET_LIST_OF_PASSENGERS = "get list of passengers";
    public static final String CREATE_PASSENGER = "create passenger with id '%s'";
    public static final String SEND_PRODUCER_MESSAGE = "send id '%s' to kafka";
    public static final String UPDATE_PASSENGER = "update passenger with id '%s'";
    public static final String DELETE_PASSENGER = "delete passenger with id '%s'";
    public static final String PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION =
            "passenger with email '%s' already exists";
    public static final String PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION =
            "passenger with phone '%s' already exists";
    public static final String NOT_FOUND_EXCEPTION = "Not found exception called: '%s'";
    public static final String CONFLICT_EXCEPTION = "Conflict exception called: '%s'";
}
