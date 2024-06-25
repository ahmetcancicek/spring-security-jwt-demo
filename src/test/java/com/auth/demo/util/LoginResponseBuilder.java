package com.auth.demo.util;

import com.auth.demo.dto.LoginResponse;

import java.util.UUID;

public class LoginResponseBuilder {
    private String accessToken = UUID.randomUUID().toString();
    private String refreshToken = UUID.randomUUID().toString();
    private String tokenType = "Bearer ";
    private long expiryDuration = 360000L;

    public LoginResponseBuilder withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public LoginResponseBuilder withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public LoginResponseBuilder withTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public LoginResponseBuilder withExpiryDuration(long expiryDuration) {
        this.expiryDuration = expiryDuration;
        return this;
    }

    public static LoginResponseBuilder generate() {
        return new LoginResponseBuilder();
    }

    public LoginResponse build() {
        return new LoginResponse(accessToken, refreshToken, tokenType, expiryDuration);
    }
}
