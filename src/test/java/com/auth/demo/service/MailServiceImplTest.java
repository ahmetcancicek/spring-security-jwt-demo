package com.auth.demo.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private MailServiceImpl mailService;


    @Test
    void givenEmailDetails_whenSendMail_thenMailIsSent() {
        mailService.sendMail("test@test.com", "test", "test");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void givenEmailTemplate_whenSendHtmlMail_thenMailIsSent() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Test</html>");

        mailService.sendHtmlMail("test@test.com", "Test Subject", "testTemplate", new Context());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }
}