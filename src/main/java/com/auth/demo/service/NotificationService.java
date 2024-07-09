package com.auth.demo.service;

public interface NotificationService {
    void sendVerificationEmail(String email, String name, String verificationURL);

    void sendPasswordResetEmail(String email, String name, String resetURL);

    void sendPasswordResetSuccessEmail(String email, String name);
}
