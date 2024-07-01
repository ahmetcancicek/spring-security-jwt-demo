package com.auth.demo.service;

import com.auth.demo.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken generateToken();

    void verifyExpiration(RefreshToken refreshToken);

    void deleteById(Long id);

    void increaseCount(RefreshToken refreshToken);

    void deleteByUsername(String username);
}
