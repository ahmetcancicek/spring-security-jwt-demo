package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for user profile")
public record ProfileRequest(

        @NotBlank
        @Size(min = 3, max = 32)
        String firstName,

        @NotBlank
        @Size(min = 3, max = 32)
        String lastName) {
}
