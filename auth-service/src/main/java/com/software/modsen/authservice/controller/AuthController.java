package com.software.modsen.authservice.controller;

import com.software.modsen.authservice.dto.request.UserLoginRequest;
import com.software.modsen.authservice.dto.request.UserRequest;
import com.software.modsen.authservice.mapper.UserLoginMapper;
import com.software.modsen.authservice.mapper.UserMapper;
import com.software.modsen.authservice.model.User;
import com.software.modsen.authservice.model.UserLogin;
import com.software.modsen.authservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakService keycloakUserService;
    private final UserMapper userMapper;
    private final UserLoginMapper userLoginMapper;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest)
    {
        User user = userMapper.toUserModel(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(keycloakUserService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        UserLogin userLogin = userLoginMapper.toUserLoginModel(userLoginRequest);
        AccessTokenResponse tokenResponse = keycloakUserService.getUserToken(userLogin);
        return ResponseEntity.ok(tokenResponse);
    }
}
