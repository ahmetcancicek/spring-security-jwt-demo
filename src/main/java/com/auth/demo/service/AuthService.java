package com.auth.demo.service;

import com.auth.demo.dto.LoginRequest;
import com.auth.demo.dto.LoginResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.model.RefreshToken;
import com.auth.demo.security.AuthUser;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    RefreshToken createRefreshToken(AuthUser authUser);
}
