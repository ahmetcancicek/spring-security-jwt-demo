package com.auth.demo.util;

import com.auth.demo.dto.LoginRequest;

public class LoginRequestBuilder {

    private String username = "johndoor";
    private String password = "H80F70g5lm5";

    public LoginRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequest build() {
        return new LoginRequest(username, password);
    }

    public static LoginRequestBuilder generate() {
        return new LoginRequestBuilder();
    }
}
