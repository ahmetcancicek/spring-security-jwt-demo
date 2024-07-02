package com.auth.demo.util;

import com.auth.demo.model.RefreshToken;
import com.auth.demo.model.User;

import java.time.Instant;

public class RefreshTokenBuilder {
    private Long id;
    private String token;
    private Integer refreshCount;
    private Instant expiryDate;
    private User user;

    public RefreshTokenBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RefreshTokenBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public RefreshTokenBuilder withRefreshCount(Integer refreshCount) {
        this.refreshCount = refreshCount;
        return this;
    }

    public RefreshTokenBuilder withExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public RefreshTokenBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public static RefreshTokenBuilder generate() {
        return new RefreshTokenBuilder();
    }

    public RefreshToken build() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setToken(token);
        refreshToken.setRefreshCount(refreshCount);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setUser(user);
        return refreshToken;
    }
}
