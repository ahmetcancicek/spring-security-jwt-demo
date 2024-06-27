package com.auth.demo.controller;

import com.auth.demo.dto.LoginRequest;
import com.auth.demo.dto.LoginResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.response.Response;
import com.auth.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API Documentation")
public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Create a new user", description = "This method creates a new user and publishes the event for email verification")
    public Response<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return respond(authService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "Login the existing user", description = "This method can be used to login operation for user and it returns the authentication tokens")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return respond(authService.login(loginRequest));
    }
}
