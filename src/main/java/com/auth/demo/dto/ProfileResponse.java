package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Response object for user profile")
public record ProfileResponse(
        String email,
        String username,
        String firstName,
        String lastName) {
}
