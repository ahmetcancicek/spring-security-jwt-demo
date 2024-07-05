package com.auth.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for checking if an email is in use")
public record EmailCheckResponse(boolean isEmailInUse, String email) {
}
