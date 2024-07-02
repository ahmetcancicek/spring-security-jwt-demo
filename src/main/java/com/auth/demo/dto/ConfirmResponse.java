package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for email confirmation")
public record ConfirmResponse(
        String username,
        String email,
        Boolean emailVerified) {
}
