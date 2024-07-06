package com.auth.demo.event;

import com.auth.demo.model.User;
import org.springframework.context.ApplicationEvent;

public class PasswordResetCompletedEvent extends ApplicationEvent {
    private User user;
    

    public PasswordResetCompletedEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
