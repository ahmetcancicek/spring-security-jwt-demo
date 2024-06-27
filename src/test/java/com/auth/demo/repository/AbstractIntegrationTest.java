package com.auth.demo.repository;

import com.auth.demo.config.AuthMySQLContainer;
import com.auth.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("integration")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AbstractIntegrationTest {

    @Autowired
    protected TestEntityManager testEntityManager;

    private User user;

    @Container
    private static final AuthMySQLContainer container = AuthMySQLContainer
            .getInstance();

    protected User createUser() {
        return user;
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("johndoor@email.com");
        user.setUsername("john.door");
        user.setPassword("H80F70g5lm5");
        user.setFirstName("John");
        user.setLastName("Door");
        user.setActive(true);
        user.setEmailVerified(true);

        testEntityManager.persist(user);
    }
}
