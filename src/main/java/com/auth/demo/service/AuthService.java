package com.auth.demo.service;

import com.auth.demo.dto.LoginRequest;
import com.auth.demo.dto.LoginResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
