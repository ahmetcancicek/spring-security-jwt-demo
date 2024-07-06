package com.auth.demo.service;

import com.auth.demo.dto.PasswordResetRequest;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.PasswordResetToken;
import com.auth.demo.model.User;
import com.auth.demo.repository.PasswordResetTokenRepository;
import com.auth.demo.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public PasswordResetToken createAndSavePasswordResetToken(User user) {
        String token = Util.generateRandomUUID();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(token);
        passwordResetToken.setClaimed(false);
        passwordResetToken.setActive(true);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Transactional
    @Override
    public PasswordResetToken claimToken(PasswordResetToken passwordResetToken) {
        User user = passwordResetToken.getUser();
        passwordResetToken.setClaimed(true);
        // TODO: All password reset token that belongs to user must be claimed

        passwordResetTokenRepository.updatePasswordResetTokenStatusByUser(false, user);

        return passwordResetToken;
    }

    @Override
    public void verifyExpiration(PasswordResetToken passwordResetToken) {
        if (passwordResetToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.error("Password reset token expired: [{}]", passwordResetToken);
            throw new BusinessException("user.passwordResetTokenExpired");
        }

        if (!passwordResetToken.getActive()) {
            log.error("Password reset token not activated: [{}]", passwordResetToken);
            throw new BusinessException("user.passwordResetTokenNotActive");
        }
    }

    @Override
    public void matchEmail(PasswordResetToken passwordResetToken, String requestEmail) {
        if (passwordResetToken.getUser().getEmail().compareToIgnoreCase(requestEmail) != 0) {
            log.error("Password reset token email is invalid for the given user: [{}]", passwordResetToken);
            throw new BusinessException("user.passwordResetTokenNotMatched");
        }
    }

    @Override
    public PasswordResetToken getValidToken(PasswordResetRequest passwordResetRequest) {
        String token = passwordResetRequest.token();
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("user.passwordResetTokenNotFound"));

        matchEmail(passwordResetToken, passwordResetRequest.email());
        verifyExpiration(passwordResetToken);
        return passwordResetToken;
    }
}
