package com.auth.demo.listener;

import com.auth.demo.event.PasswordResetCompletedEvent;
import com.auth.demo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetCompletedEventListener implements ApplicationListener<PasswordResetCompletedEvent> {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetCompletedEventListener.class);
    private final NotificationService notificationService;

    public PasswordResetCompletedEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @Override
    public void onApplicationEvent(PasswordResetCompletedEvent event) {
        log.info("Event received: {}", event.toString());
        String email = event.getUser().getEmail();
        String name = event.getUser().getFirstName();
        notificationService.sendPasswordResetSuccessEmail(email, name);
    }
}
