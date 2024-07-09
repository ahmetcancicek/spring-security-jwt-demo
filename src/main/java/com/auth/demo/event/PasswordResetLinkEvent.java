package com.auth.demo.event;

import com.auth.demo.model.User;
import org.springframework.context.ApplicationEvent;

public class PasswordResetLinkEvent extends ApplicationEvent {
    private User user;
    private String passwordResetURL;

    public PasswordResetLinkEvent(User user, String link) {
        super(user);
        this.user = user;
        this.passwordResetURL = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getPasswordResetURL() {
        return passwordResetURL;
    }

    public void setPasswordResetURL(String passwordResetURL) {
        this.passwordResetURL = passwordResetURL;
    }
}
