package com.auth.demo.util;

import com.auth.demo.dto.ConfirmResponse;

public class ConfirmResponseBuilder {
    private String email = "johndoor@email.com";
    private String username = "johndoor";
    private boolean emailVerified = true;

    public ConfirmResponseBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ConfirmResponseBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ConfirmResponseBuilder withEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public static ConfirmResponseBuilder generate() {
        return new ConfirmResponseBuilder();
    }

    public ConfirmResponse build() {
        return new ConfirmResponse(username, email, emailVerified);
    }
}
