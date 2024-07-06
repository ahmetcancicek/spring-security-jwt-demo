package com.auth.demo.util;

import com.auth.demo.dto.PasswordResetRequest;

public class PasswordResetRequestBuilder {
    private String email;
    private String password;
    private String confirmPassword;
    private String token;

    public PasswordResetRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public PasswordResetRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public PasswordResetRequestBuilder withConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public PasswordResetRequestBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public static PasswordResetRequestBuilder generate() {
        return new PasswordResetRequestBuilder();
    }

    public PasswordResetRequest build() {
        return new PasswordResetRequest(email, password, confirmPassword, token);
    }
}
