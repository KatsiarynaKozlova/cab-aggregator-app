package com.software.modsen.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String role;
}
