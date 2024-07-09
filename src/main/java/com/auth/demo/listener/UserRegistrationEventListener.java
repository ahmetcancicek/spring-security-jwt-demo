package com.auth.demo.listener;

import com.auth.demo.event.UserRegistrationEvent;
import com.auth.demo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationEventListener.class);
    private final NotificationService notificationService;

    public UserRegistrationEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        log.info("Event received: {}", event.toString());
        String email = event.getUser().getEmail();
        String name = event.getUser().getFirstName();
        String setUrl = event.getVerificationURL();
        notificationService.sendVerificationEmail(email, name, setUrl);
    }
}
