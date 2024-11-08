package com.software.modsen.authservice.dto.request;

public record UserRequest(String username, String email, String phone, String password, String role) {
}
