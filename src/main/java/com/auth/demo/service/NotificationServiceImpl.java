package com.auth.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final MailService mailService;

    public NotificationServiceImpl(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void sendVerificationEmail(String email, String name, String verificationURL) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationURL", verificationURL);
        mailService.sendHtmlMail(email, "Email Verification", "verificationEmail", context);
        logger.info("Email verification email sent to {}", email);
    }

    @Override
    public void sendPasswordResetEmail(String email, String name, String resetURL) {
        Context context = new Context();
        context.setVariable("resetURL", resetURL);
        context.setVariable("name", name);
        mailService.sendHtmlMail(email, "Password Reset", "passwordResetEmail", context);
        logger.info("Password reset email sent to {}", email);
    }

    @Override
    public void sendPasswordResetSuccessEmail(String email, String name) {
        Context context = new Context();
        context.setVariable("name", name);
        mailService.sendHtmlMail(email, "Password Reset", "passwordResetSuccessEmail", context);
        logger.info("Password reset success email sent to {}", email);
    }
}
