package com.auth.demo.service;

import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.RefreshToken;
import com.auth.demo.repository.RefreshTokenRepository;
import com.auth.demo.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokeServiceImpl implements RefreshTokenService {

    private final static Logger logger = LoggerFactory.getLogger(RefreshTokeServiceImpl.class);
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.token.refresh.duration}")
    private Long refreshTokenDurationMs;

    public RefreshTokeServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("refresh.token.notFound"));
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken generateToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(Util.generateRandomUUID());
        refreshToken.setRefreshCount(0);
        return refreshToken;
    }

    @Override
    public void verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new BusinessException("refresh.token.expired");
        }
    }

    @Override
    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }

    @Override
    public void increaseCount(RefreshToken refreshToken) {
        refreshToken.incrementRefreshCount();
        save(refreshToken);
    }

    @Override
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }

}
