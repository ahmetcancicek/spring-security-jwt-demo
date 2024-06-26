package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for user login")
public record LoginRequest(
        @NotBlank
        String username,

        @NotBlank
        String password) {
}
