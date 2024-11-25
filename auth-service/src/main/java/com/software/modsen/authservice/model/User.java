package com.software.modsen.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private final Boolean isEnabled = Boolean.TRUE;
    private final Boolean isEmailVerified = Boolean.TRUE;
}
