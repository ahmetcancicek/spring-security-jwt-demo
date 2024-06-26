package com.auth.demo.util;

import com.auth.demo.dto.ProfileRequest;

public class ProfileRequestBuilder {

    private String firstName = "John";
    private String lastName = "Door";

    public ProfileRequestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ProfileRequestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public static ProfileRequestBuilder generate() {
        return new ProfileRequestBuilder();
    }

    public ProfileRequest build() {
        return new ProfileRequest(firstName, lastName);
    }
}
