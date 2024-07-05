package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for checking if an username is in use")
public record UsernameCheckResponse(boolean isUsernameInUse, String username) {
}
