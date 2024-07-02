package com.auth.demo.event;

import com.auth.demo.model.User;
import org.springframework.context.ApplicationEvent;


public class UserRegistrationEvent extends ApplicationEvent {

    private User user;
    private String token;


    public UserRegistrationEvent(User user, String token) {
        super(user);
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
