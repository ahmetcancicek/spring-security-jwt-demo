package com.auth.demo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email
        @NotBlank
        @Size(min = 4, max = 100)
        String email,

        @NotBlank
        @Size(min = 4, max = 50)
        String username,

        @NotBlank
        @Size(min = 4, max = 30)
        String password,

        @NotNull
        boolean registerAsAdmin,

        @Size(min = 0, max = 32)
        String firstName,

        @Size(min = 0, max = 32)
        String lastName) {
}
