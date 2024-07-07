package com.auth.demo.service;

import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.User;

public interface EmailVerificationTokenService {
    EmailVerificationToken saveVerificationToken(User user, String token);

    EmailVerificationToken createAndSaveVerificationToken(User user);

    EmailVerificationToken findByToken(String token);

    EmailVerificationToken updateExistingToken(EmailVerificationToken emailVerificationToken);

    EmailVerificationToken save(EmailVerificationToken emailVerificationToken);

    String generateNewToken();

    void verifyExpiration(EmailVerificationToken existingToken);

}
