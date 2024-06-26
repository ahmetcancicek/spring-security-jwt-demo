package com.auth.demo.converter;

import com.auth.demo.dto.ProfileResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User fromRegisterRequestToUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.email());
        user.setUsername(registerRequest.username());
        user.setPassword(registerRequest.password());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        return user;
    }

    public RegisterResponse fromUserToRegisterResponse(User user) {
        return new RegisterResponse(user.getEmail(), user.getUsername(), user.getEmailVerified(), user.getActive(), user.getFirstName(), user.getLastName());
    }

    public ProfileResponse fromUserToProfileResponse(User user) {
        return new ProfileResponse(user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName());
    }
}
