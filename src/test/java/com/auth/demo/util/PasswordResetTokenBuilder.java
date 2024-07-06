package com.auth.demo.util;

import com.auth.demo.model.PasswordResetToken;
import com.auth.demo.model.User;

import java.time.Instant;

public class PasswordResetTokenBuilder {
    private Long id;
    private String token;
    private User user;
    private Instant expiryDate;
    private Boolean active;
    private Boolean claimed;

    public PasswordResetTokenBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PasswordResetTokenBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public PasswordResetTokenBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public PasswordResetTokenBuilder withExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public PasswordResetTokenBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public PasswordResetTokenBuilder withClaimed(Boolean claimed) {
        this.claimed = claimed;
        return this;
    }

    public static PasswordResetTokenBuilder generate() {
        return new PasswordResetTokenBuilder();
    }

    public PasswordResetToken build() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(id);
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(expiryDate);
        passwordResetToken.setActive(active);
        passwordResetToken.setClaimed(claimed);
        return passwordResetToken;
    }
}
