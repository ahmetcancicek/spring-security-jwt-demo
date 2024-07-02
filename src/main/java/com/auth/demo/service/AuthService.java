package com.auth.demo.service;

import com.auth.demo.dto.*;
import com.auth.demo.model.RefreshToken;
import com.auth.demo.security.AuthUser;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    RefreshToken createAndSaveRefreshToken(AuthUser authUser);

    LoginResponse refreshToken(TokenRefreshRequest tokenRefreshRequest);
}
