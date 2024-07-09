package com.auth.demo.service;

import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface MailService {
    void sendMail(String to, String subject, String text);

    void sendHtmlMail(String to, String subject, String templateName, Context context);
}
