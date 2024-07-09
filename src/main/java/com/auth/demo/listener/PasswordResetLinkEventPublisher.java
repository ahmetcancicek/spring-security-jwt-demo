package com.auth.demo.listener;

import com.auth.demo.event.PasswordResetLinkEvent;
import com.auth.demo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetLinkEventPublisher implements ApplicationListener<PasswordResetLinkEvent> {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetLinkEventPublisher.class);
    private final NotificationService notificationService;

    public PasswordResetLinkEventPublisher(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @Override
    public void onApplicationEvent(PasswordResetLinkEvent event) {
        log.info("Event received: {}", event.toString());
        String email = event.getUser().getEmail();
        String name = event.getUser().getFirstName();
        String setUrl = event.getPasswordResetURL();
        notificationService.sendPasswordResetEmail(email, name, setUrl);
    }
}
