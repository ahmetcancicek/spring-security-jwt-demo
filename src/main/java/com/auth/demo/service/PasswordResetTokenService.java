package com.auth.demo.service;

import com.auth.demo.dto.PasswordResetRequest;
import com.auth.demo.model.PasswordResetToken;
import com.auth.demo.model.User;

public interface PasswordResetTokenService {
    PasswordResetToken createAndSavePasswordResetToken(User user);

    PasswordResetToken claimToken(PasswordResetToken passwordResetToken);

    void verifyExpiration(PasswordResetToken passwordResetToken);

    void matchEmail(PasswordResetToken passwordResetToken, String requestEmail);

    PasswordResetToken getValidToken(PasswordResetRequest passwordResetRequest);
}
