package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for user login")
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiryDuration) {

}
