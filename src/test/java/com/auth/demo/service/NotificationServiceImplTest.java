package com.auth.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private MailService mailService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void givenEmailRequest_whenSendVerificationEmail_thenMailIsSent() {
        notificationService.sendVerificationEmail("test@test.com", "test", "testURL");

        verify(mailService, times(1)).sendHtmlMail(eq("test@test.com"), any(), any(), any());
    }

    @Test
    void givenEmailRequest_whenSendPasswordResetEmail_thenMailIsSent() {
        notificationService.sendPasswordResetEmail("test@test.com", "test", "testURL");

        verify(mailService, times(1)).sendHtmlMail(eq("test@test.com"), any(), any(), any());
    }

    @Test
    void givenEmailRequest_whenSendPasswordResetSuccessEmail_thenMailIsSent() {
        notificationService.sendPasswordResetSuccessEmail("test@test.com", "test");

        verify(mailService, times(1)).sendHtmlMail(eq("test@test.com"), any(), any(), any());
    }

}