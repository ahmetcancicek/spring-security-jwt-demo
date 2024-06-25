package com.auth.demo.converter;

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
        RegisterResponse registerResponse = new RegisterResponse(user.getEmail(), user.getUsername(), user.getEmailVerified(), user.getActive(), user.getFirstName(), user.getLastName());
        return registerResponse;
    }
}
