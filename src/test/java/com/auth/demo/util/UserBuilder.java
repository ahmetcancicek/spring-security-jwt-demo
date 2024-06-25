package com.auth.demo.util;

import com.auth.demo.model.Role;
import com.auth.demo.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {
    private Long id = 1L;
    private String email = "johndoor@email.com";
    private String username = "johndoor";
    private String password = "H80F70g5lm5";
    private String firstName = "John";
    private String lastName = "Door";
    private Boolean active = true;
    private Boolean emailVerified = true;
    private Set<Role> roles = new HashSet<>();

    private UserBuilder() {

    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public UserBuilder withEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public UserBuilder withRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setEmailVerified(emailVerified);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(active);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(roles);
        return user;
    }

    public static UserBuilder generate() {
        return new UserBuilder();
    }
}
