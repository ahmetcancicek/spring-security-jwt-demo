USE db_authdemo;

INSERT INTO users_seq (next_val) VALUES (1);

INSERT INTO role_seq (next_val) VALUES (1);

INSERT INTO users_roles_seq (next_val) VALUES (1);

INSERT INTO email_verification_token_seq (next_val) VALUES (1);

INSERT INTO refresh_token_seq (next_val) VALUES (1);

INSERT INTO password_reset_token_seq (next_val) VALUES (1);

INSERT INTO role(id, name) VALUES (1, 'ROLE_USER');

INSERT INTO role(id, name) VALUES (2, 'ROLE_ADMIN');