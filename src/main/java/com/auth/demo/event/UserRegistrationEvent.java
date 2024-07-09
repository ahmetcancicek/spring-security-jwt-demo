package com.auth.demo.event;

import com.auth.demo.model.User;
import org.springframework.context.ApplicationEvent;


public class UserRegistrationEvent extends ApplicationEvent {

    private User user;
    private String verificationURL;


    public UserRegistrationEvent(User user, String verificationURL) {
        super(user);
        this.user = user;
        this.verificationURL = verificationURL;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVerificationURL() {
        return verificationURL;
    }

    public void setVerificationURL(String verificationURL) {
        this.verificationURL = verificationURL;
    }
}
