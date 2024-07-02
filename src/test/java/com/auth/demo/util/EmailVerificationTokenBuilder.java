package com.auth.demo.util;

import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.TokenStatus;
import com.auth.demo.model.User;

import java.time.Instant;

public class EmailVerificationTokenBuilder {

    private Long id = 1L;
    private String token;
    private User user;
    private Instant expiryDate;
    private TokenStatus tokenStatus;

    public EmailVerificationTokenBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EmailVerificationTokenBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public EmailVerificationTokenBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public EmailVerificationTokenBuilder withExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public EmailVerificationTokenBuilder withTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
        return this;
    }

    public static EmailVerificationTokenBuilder generate() {
        return new EmailVerificationTokenBuilder();
    }

    public EmailVerificationToken build() {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setId(id);
        emailVerificationToken.setToken(token);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiryDate(expiryDate);
        emailVerificationToken.setTokenStatus(tokenStatus);
        return emailVerificationToken;
    }
}
