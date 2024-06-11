CREATE DATABASE IF NOT EXISTS db_authdemo CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

USE db_authdemo;

CREATE TABLE users_seq
(
    next_val BIGINT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS users
(
    id                BIGINT       NOT NULL,
    created_at        datetime     NOT NULL,
    updated_at        datetime     NOT NULL,
    email             VARCHAR(100) NOT NULL,
    username          VARCHAR(50)  NOT NULL,
    password          VARCHAR(255) NOT NULL,
    first_name        VARCHAR(32)  NULL,
    last_name         VARCHAR(32)  NULL,
    is_active         BIT(1)       NOT NULL,
    is_email_verified BIT(1)       NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email),
    CONSTRAINT uc_users_username UNIQUE (username)
) ENGINE = INNODB;

CREATE TABLE role_seq
(
    next_val BIGINT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS role
(
    id   BIGINT                           NOT NULL,
    name enum ('ROLE_ADMIN', 'ROLE_USER') NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id),
    CONSTRAINT uc_role_name UNIQUE (name)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS users_roles
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    role_id BIGINT                NOT NULL DEFAULT 1 COMMENT '1:ROLE_USER, 2:ROLE_ADMIN',
    user_id BIGINT                NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (id),
    CONSTRAINT fk_users_roles_on_role FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_users_roles_on_users FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = INNODB;

CREATE TABLE email_verification_token_seq
(
    next_val BIGINT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS email_verification_token
(
    id           BIGINT                                      NOT NULL,
    created_at   datetime                                    NOT NULL,
    updated_at   datetime                                    NULL,
    token        VARCHAR(255)                                NOT NULL,
    user_id      BIGINT                                      NOT NULL,
    expiry_date  datetime                                    NOT NULL,
    token_status enum ('STATUS_PENDING', 'STATUS_CONFIRMED') NULL,
    CONSTRAINT pk_email_verification_token PRIMARY KEY (id),
    CONSTRAINT uc_email_verification_token_token UNIQUE (token),
    CONSTRAINT fk_email_verification_token_on_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = INNODB;

CREATE TABLE refresh_token_seq
(
    next_val BIGINT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS refresh_token
(
    id            BIGINT       NOT NULL,
    created_at    datetime     NOT NULL,
    updated_at    datetime     NULL,
    token         VARCHAR(255) NOT NULL,
    refresh_count INTEGER      NOT NULL,
    expiry_date   datetime     NOT NULL,
    user_id       BIGINT       NOT NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id),
    CONSTRAINT uc_refresh_token_token UNIQUE (token),
    CONSTRAINT fk_refresh_token_on_users FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = INNODB;

CREATE TABLE password_reset_token_seq
(
    next_val BIGINT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS password_reset_token
(
    id          BIGINT       NOT NULL,
    created_at  datetime     NOT NULL,
    updated_at  datetime     NULL,
    token       VARCHAR(255) NOT NULL,
    expiry_date datetime     NOT NULL,
    user_id     BIGINT       NOT NULL,
    is_active   BIT          NOT NULL,
    is_claimed  BIT          NOT NULL,
    CONSTRAINT pk_password_reset_token PRIMARY KEY (id),
    CONSTRAINT uc_password_reset_token_token UNIQUE (token),
    CONSTRAINT fk_password_reset_token_on_users FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = INNODB;