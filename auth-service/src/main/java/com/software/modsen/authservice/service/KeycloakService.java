package com.software.modsen.authservice.service;

import com.software.modsen.authservice.dto.request.FeignUserRequest;
import com.software.modsen.authservice.dto.response.UserResponse;
import com.software.modsen.authservice.exception.InvalidUserDataException;
import com.software.modsen.authservice.exception.ServiceUnAvailableException;
import com.software.modsen.authservice.exception.UserAlreadyExistException;
import com.software.modsen.authservice.exception.WrongCredentialsException;
import com.software.modsen.authservice.model.Role;
import com.software.modsen.authservice.model.User;
import com.software.modsen.authservice.model.UserLogin;
import com.software.modsen.authservice.util.ExceptionMessages;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.software.modsen.authservice.util.ExceptionMessages.UNAUTHORIZED_EXCEPTION;
import static com.software.modsen.authservice.util.KeycloakConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final PassengerClientFallback passengerClient;
    private final DriverClientFallback driverClient;
    private final Keycloak keycloakConfig = KeycloakBuilder.builder()
            .serverUrl(SERVER_URL)
            .realm(REALM)
            .grantType(OAuth2Constants.PASSWORD)
            .clientId(KEYCLOAK_CLIENT_ID)
            .clientSecret(KEYCLOAK_CLIENT_SECRET)
            .username(KEYCLOAK_ADMIN_USERNAME)
            .password(KEYCLOAK_ADMIN_PASSWORD)
            .build();

    public User createUser(FeignUserRequest userRequest, User user) {
        UserResponse userResponseClient = createUserFeign(user.getRole(), userRequest);
        try {
            user.setId(userResponseClient.id());
            return createUserKeycloak(user);
        }catch (UserAlreadyExistException e) {
            deleteUserFeign(user.getRole(), userResponseClient.id());
            throw new UserAlreadyExistException(e.getMessage());
        } catch (InvalidUserDataException e) {
            deleteUserFeign(user.getRole(), userResponseClient.id());
            throw new InvalidUserDataException(e.getMessage());
        } catch (Exception e) {
            deleteUserFeign(user.getRole(), userResponseClient.id());
            throw new ServiceUnAvailableException(ExceptionMessages.SERVICE_IS_NOT_AVAILABLE);
        }
    }

    private void deleteUserFeign(Role role, Long id){
        if (role.name().equals(Role.PASSENGER.name())) {
            passengerClient.deleteUser(id);
        }
        else if (role.name().equals(Role.DRIVER.name())) {
            driverClient.deleteUser(id);
        } else {
            throw new InvalidUserDataException(ExceptionMessages.INVALID_USER_DATA);
        }
    }
    private UserResponse createUserFeign(Role role, FeignUserRequest userRequest) {
        if (role.name().equals(Role.PASSENGER.name())) {
            return passengerClient.createUser(userRequest);
        }
        else if (role.name().equals(Role.DRIVER.name())) {
            return driverClient.createUser(userRequest);
        } else {
            throw new InvalidUserDataException(ExceptionMessages.INVALID_USER_DATA);
        }
    }

    private User createUserKeycloak(User user) {
        UserRepresentation userRepresentation = createUserRepresentation(user);

        try (Response response = keycloakConfig.realm(REALM).users().create(userRepresentation)) {
            if (response.getStatus() == 400) {
                throw new InvalidUserDataException(ExceptionMessages.INVALID_USER_DATA);
            }

            String locationHeader = response.getHeaderString(LOCATION);
            if (locationHeader == null) {
                throw new UserAlreadyExistException(ExceptionMessages.ALREADY_EXIST_EXCEPTION);
            }

            String id = keycloakConfig.realm(REALM).users().search(user.getUsername()).get(0).getId();
            RoleRepresentation roleRep = keycloakConfig.realm(REALM).roles().get(user.getRole().toString()).toRepresentation();
            keycloakConfig.realm(REALM).users().get(id).roles().realmLevel().add(Arrays.asList(roleRep));

        } catch (UserAlreadyExistException e) {
            log.info(e.getMessage());
            throw new UserAlreadyExistException(e.getMessage());
        } catch (InvalidUserDataException e) {
            log.info(e.getMessage());
            throw new InvalidUserDataException(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new ServiceUnAvailableException(ExceptionMessages.SERVICE_IS_NOT_AVAILABLE);
        }
        return user;
    }

    public AccessTokenResponse getUserToken(UserLogin userLogin) {
        try {
            return KeycloakBuilder.builder()
                    .serverUrl(SERVER_URL)
                    .realm(REALM)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(KEYCLOAK_CLIENT_ID)
                    .clientSecret(KEYCLOAK_CLIENT_SECRET)
                    .username(userLogin.getUsername())
                    .password(userLogin.getPassword())
                    .build()
                    .tokenManager()
                    .getAccessToken();
        } catch (Exception e) {
            throw new WrongCredentialsException(UNAUTHORIZED_EXCEPTION);
        }
    }

    public UserRepresentation createUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(user.getIsEnabled());
        userRepresentation.setEmailVerified(user.getIsEmailVerified());

        Map<String, List<String>> attributesMap = new HashMap<>();
        attributesMap.put(ID, List.of(user.getId().toString()));
        attributesMap.put(PHONE, List.of(user.getPhone()));
        userRepresentation.setAttributes(attributesMap);

        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(user.getPassword());
        passwordCredentials.setTemporary(false);

        userRepresentation.setCredentials(List.of(passwordCredentials));

        return userRepresentation;
    }
}
