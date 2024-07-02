package com.auth.demo.service;

import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.RefreshToken;
import com.auth.demo.repository.RefreshTokenRepository;
import com.auth.demo.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new BusinessException("user.refreshTokenNotFound"));
    }

    @Transactional
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
        logger.info("Refresh token generated: [{}]", refreshToken);
        return refreshToken;
    }

    @Transactional
    @Override
    public void verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.deleteById(refreshToken.getId());
            logger.error("Refresh token expired: [{}]", refreshToken);
            throw new BusinessException("user.refreshTokenExpired");
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        refreshTokenRepository.findById(id)
                .orElseThrow(() -> new BusinessException("user.refreshTokenNotFound"));

        logger.info("Delete refresh token: [{}]", id);
        refreshTokenRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void increaseCount(RefreshToken refreshToken) {
        logger.info("Increase refresh token count: [{}]", refreshToken);
        refreshToken.incrementRefreshCount();
        save(refreshToken);
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }
}
