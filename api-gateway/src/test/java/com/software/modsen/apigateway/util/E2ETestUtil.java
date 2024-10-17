package com.software.modsen.apigateway.util;

public final class E2ETestUtil {
    private E2ETestUtil() {
    }

    public static final String DEFAULT_CAR_REQUEST_BODY = "{ \"model\": \"%s\", \"licensePlate\": \"%s\", \"year\": %d, \"color\": \"%s\" }";
    public static final String DEFAULT_DRIVER_REQUEST_BODY = "{ \"name\": \"%s\", \"email\": \"%s\", \"phone\": \"%s\", \"sex\": \"%s\", \"carId\": %d }";
    public static final String DEFAULT_PASSENGER_REQUEST_BODY = "{ \"name\": \"%s\", \"email\": \"%s\", \"phone\": \"%s\"}";
    public static final String DEFAULT_RIDE_REQUEST_BODY = "{ \"driverId\" : %d, \"passengerId\": %d, \"routeStart\": \"%s\", \"routeEnd\": \"%s\" }";
    public static final String DEFAULT_RATING_REQUEST_BODY = "{ \"rideId\": %d , \"rate\" : %d , \"comment\" : \"%s\" }";
}
