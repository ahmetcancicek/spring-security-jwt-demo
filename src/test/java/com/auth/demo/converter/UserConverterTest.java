package com.auth.demo.converter;

import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.model.User;
import com.auth.demo.util.RegisterRequestBuilder;
import com.auth.demo.util.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();
    }

    @Test
    public void givenRegisterRegister_whenFromRegisterRequestToUser_thenReturnUser() {
        RegisterRequest registerRequest = RegisterRequestBuilder.generate().build();

        User user = userConverter.fromRegisterRequestToUser(registerRequest);

        assertThat(user.getEmail()).isEqualTo(registerRequest.email());
        assertThat(user.getUsername()).isEqualTo(registerRequest.username());
        assertThat(user.getPassword()).isEqualTo(registerRequest.password());
        // TODO: Add the check control for the roles
        assertThat(user.getFirstName()).isEqualTo(registerRequest.firstName());
        assertThat(user.getLastName()).isEqualTo(registerRequest.lastName());
    }

    @Test
    public void givenUser_whenFromUserToRegisterResponse_thenReturnRegisterResponse() {
        User user = UserBuilder.generate().build();

        RegisterResponse registerResponse = userConverter.fromUserToRegisterResponse(user);

        assertThat(registerResponse.email()).isEqualTo(user.getEmail());
        assertThat(registerResponse.username()).isEqualTo(user.getUsername());
        assertThat(registerResponse.emailVerified()).isEqualTo(user.getEmailVerified());
        assertThat(registerResponse.active()).isEqualTo(user.getActive());
        assertThat(registerResponse.firstName()).isEqualTo(user.getFirstName());
        assertThat(registerResponse.lastName()).isEqualTo(user.getLastName());
    }
}