package com.software.modsen.authservice.util;

import org.springframework.stereotype.Component;

@Component
public final class KeycloakConstants {
    public static final String PHONE = "phone";
    public static final String ID = "id";
    public static final String LOCATION = "Location";
    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";
    public static final String PREFIX_ROLE = "ROLE_";
    public static final String SERVER_URL = "http://keycloak:8100";
    public static final String REALM = "cab-aggregator-realm";
    public static final String KEYCLOAK_CLIENT_ID = "spring-gateway-client";
    public static final String KEYCLOAK_CLIENT_SECRET = "R8zZaZmV1sm3vlTGa1Woq7uUbt6LbaAd";
    public static final String KEYCLOAK_ADMIN_USERNAME = "admin";
    public static final String KEYCLOAK_ADMIN_PASSWORD = "admin";
}
