package com.software.modsen.authservice.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.software.modsen.authservice.util.KeycloakConstants.KEYCLOAK_ADMIN_PASSWORD;
import static com.software.modsen.authservice.util.KeycloakConstants.KEYCLOAK_ADMIN_USERNAME;
import static com.software.modsen.authservice.util.KeycloakConstants.KEYCLOAK_CLIENT_ID;
import static com.software.modsen.authservice.util.KeycloakConstants.KEYCLOAK_CLIENT_SECRET;
import static com.software.modsen.authservice.util.KeycloakConstants.REALM;
import static com.software.modsen.authservice.util.KeycloakConstants.SERVER_URL;

@Component
public class AuthRequestInterceptor implements RequestInterceptor {
    private final Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(SERVER_URL)
            .realm(REALM)
            .grantType(OAuth2Constants.PASSWORD)
            .clientId(KEYCLOAK_CLIENT_ID)
            .clientSecret(KEYCLOAK_CLIENT_SECRET)
            .username(KEYCLOAK_ADMIN_USERNAME)
            .password(KEYCLOAK_ADMIN_PASSWORD)
            .build();

    private AccessTokenResponse token;
    private synchronized void refreshToken() {
        this.token = keycloak.tokenManager().getAccessToken();
    }

    @Scheduled(fixedRateString = "300000")
    public void scheduledTokenRefresh() {
        refreshToken();
    }
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            requestTemplate.header("Authorization", "Bearer " + token.getToken());
        }
    }
}
