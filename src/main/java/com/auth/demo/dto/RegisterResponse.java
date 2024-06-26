package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for user registration")
public record RegisterResponse(
        String email,
        String username,
        boolean emailVerified,
        boolean active,
        String firstName,
        String lastName) {
}
