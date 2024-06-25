package com.auth.demo.util;

import com.auth.demo.dto.RegisterRequest;

public class RegisterRequestBuilder {
    private String email = "johndoor@email.com";
    private String username = "johndoor";
    private String password = "H80F70g5lm5";
    private boolean registerAsAdmin = true;
    private String firstName = "John";
    private String lastName = "Door";

    public RegisterRequestBuilder() {

    }


    public RegisterRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public RegisterRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterRequestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public RegisterRequestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public RegisterRequestBuilder withRegisterAsAdmin(Boolean registerAsAdmin) {
        this.registerAsAdmin = registerAsAdmin;
        return this;
    }

    public RegisterRequest build() {
        return new RegisterRequest(email, username, password, registerAsAdmin, firstName, lastName);
    }

    public static RegisterRequestBuilder generate() {
        return new RegisterRequestBuilder();
    }
}
