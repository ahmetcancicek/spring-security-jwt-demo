package com.auth.demo.util;

import com.auth.demo.dto.ProfileResponse;

public class ProfileResponseBuilder {
    private String email = "johndoor@email.com";
    private String username = "johndoor";
    private String firstName = "John";
    private String lastName = "Door";

    public ProfileResponseBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ProfileResponseBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ProfileResponseBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ProfileResponseBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public static ProfileResponseBuilder generate() {
        return new ProfileResponseBuilder();
    }

    public ProfileResponse build() {
        return new ProfileResponse(email, username, firstName, lastName);
    }
}
