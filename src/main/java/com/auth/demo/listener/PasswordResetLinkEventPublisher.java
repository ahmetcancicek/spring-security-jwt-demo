package com.auth.demo.listener;

import com.auth.demo.event.PasswordResetLinkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetLinkEventPublisher implements ApplicationListener<PasswordResetLinkEvent> {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetLinkEventPublisher.class);

    public PasswordResetLinkEventPublisher() {
    }

    @Async
    @Override
    public void onApplicationEvent(PasswordResetLinkEvent event) {
        // TODO: Add the send email reset link to the user
        System.out.println("Reset link event received: " + event.getUser());
        System.out.println("Link: " + event.getLink());
    }
}
