package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for password reset")
public record PasswordResetRequest(

        @Email
        @NotBlank
        @Size(min = 4, max = 100)
        String email,

        @NotBlank
        @Size(min = 4, max = 30)
        String password,

        @NotBlank
        @Size(min = 4, max = 30)
        String confirmPassword,

        @NotBlank
        @Size(min = 3, max = 100)
        String token) {
}
