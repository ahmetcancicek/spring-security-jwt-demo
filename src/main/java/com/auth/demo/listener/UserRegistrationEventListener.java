package com.auth.demo.listener;

import com.auth.demo.event.UserRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationEventListener.class);

    public UserRegistrationEventListener() {
    }

    @Async
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        // TODO: Add the send email verification to the user as email
        System.out.println("Registration event received: " + event.getUser());
        System.out.println("Token: " + event.getToken());
    }
}
