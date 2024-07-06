package com.auth.demo.event;

import com.auth.demo.model.User;
import org.springframework.context.ApplicationEvent;

public class PasswordResetLinkEvent extends ApplicationEvent {
    private User user;
    private String link;

    public PasswordResetLinkEvent(User user, String link) {
        super(user);
        this.user = user;
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
