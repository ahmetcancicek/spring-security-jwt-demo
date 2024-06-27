package com.auth.demo.config;

import org.testcontainers.containers.MySQLContainer;

public class AuthMySQLContainer extends MySQLContainer<AuthMySQLContainer> {

    private static final String IMAGE_VERSION = "mysql:8.3.0";
    private static AuthMySQLContainer container;

    private AuthMySQLContainer() {
        super(IMAGE_VERSION);
    }

    public static AuthMySQLContainer getInstance() {
        if (container == null)
            container = new AuthMySQLContainer();
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {

    }


}
