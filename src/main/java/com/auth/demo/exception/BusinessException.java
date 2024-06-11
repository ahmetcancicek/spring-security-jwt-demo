package com.auth.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class BusinessException extends RuntimeException {

    private final String key;
    private final String[] args;

    public BusinessException(String key) {
        super(key);
        this.key = key;
        args = new String[0];
    }

    public BusinessException(String key, String... args) {
        super(key);
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public String[] getArgs() {
        return args;
    }
}
