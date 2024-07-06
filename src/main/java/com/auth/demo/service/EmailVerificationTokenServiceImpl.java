package com.auth.demo.service;

import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.TokenStatus;
import com.auth.demo.model.User;
import com.auth.demo.repository.EmailVerificationTokenRepository;
import com.auth.demo.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private static final Logger log = LoggerFactory.getLogger(EmailVerificationToken.class);
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Value("${app.token.email.verification.duration}")
    private Long emailVerificationTokenExpiryDuration;

    public EmailVerificationTokenServiceImpl(EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    @Override
    public EmailVerificationToken saveVerificationToken(User user, String token) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setUser(user);
        emailVerificationToken.setToken(token);
        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        log.info("Email verification token created [{}]", emailVerificationToken);
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public EmailVerificationToken createAndSaveVerificationToken(User user) {
        return saveVerificationToken(user, generateNewToken());
    }

    @Override
    public EmailVerificationToken findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("user.emailTokenNotFound"));
    }

    @Override
    public EmailVerificationToken updateExistingToken(EmailVerificationToken existingToken) {
        existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        log.info("Email verification token updated [{}]", existingToken);
        return save(existingToken);
    }

    @Override
    public EmailVerificationToken save(EmailVerificationToken token) {
        return emailVerificationTokenRepository.save(token);
    }

    @Override
    public String generateNewToken() {
        return Util.generateRandomUUID();
    }

    @Override
    public void verifyExpiration(EmailVerificationToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new BusinessException("user.emailTokenExpired");
        }
    }
}
