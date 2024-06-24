package com.auth.demo.dto;

public record RegisterResponse(
        String email,
        String username,
        boolean emailVerified,
        boolean active,
        String firstName,
        String lastName) {
}
