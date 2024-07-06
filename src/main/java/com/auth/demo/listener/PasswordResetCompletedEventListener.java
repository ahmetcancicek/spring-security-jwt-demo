package com.auth.demo.listener;

import com.auth.demo.event.PasswordResetCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetCompletedEventListener implements ApplicationListener<PasswordResetCompletedEvent> {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetCompletedEventListener.class);

    @Async
    @Override
    public void onApplicationEvent(PasswordResetCompletedEvent event) {
        // TODO: Add the send email for password reset completed successfully
        System.out.println("Password reset completed event received: " + event.getUser());
    }
}
