package com.auth.demo.util;

import com.auth.demo.dto.RegisterResponse;

public class RegisterResponseBuilder {
    private String email = "johndoor@email.com";
    private String username = "johndoor";
    private String firstName = "John";
    private String lastName = "Door";
    private boolean emailVerified = true;
    private boolean active = true;

    private RegisterResponseBuilder() {

    }


    public RegisterResponseBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterResponseBuilder withUsername(String username) {
        this.username = username;
        return this;
    }


    public RegisterResponseBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public RegisterResponseBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public RegisterResponseBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public RegisterResponseBuilder withEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public static RegisterResponseBuilder generate() {
        return new RegisterResponseBuilder();
    }

    public RegisterResponse build() {
        return new RegisterResponse(email, username, emailVerified, active, firstName, lastName);
    }

}
