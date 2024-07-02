package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for token refresh")
public record TokenRefreshRequest(

        @NotBlank
        @Size(min = 3, max = 100)
        String refreshToken) {
}
