package com.auth.demo.repository;

import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.TokenStatus;
import com.auth.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmailVerificationTokenRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    private EmailVerificationToken createEmailVerificationToken() {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setUser(createUser());
        emailVerificationToken.setToken(UUID.randomUUID().toString());
        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        emailVerificationToken.setExpiryDate(Instant.now().plusMillis(3600000));
        return emailVerificationToken;
    }

    @Test
    public void whenSave_thenReturnSavedEmailVerificationToken() {
        EmailVerificationToken emailVerificationToken = createEmailVerificationToken();
        emailVerificationTokenRepository.save(emailVerificationToken);

        // then
        assertThat(emailVerificationToken.getId()).isNotNull();
        assertThat(emailVerificationToken.getId()).isGreaterThan(0L);
    }

    @Test
    public void whenFindById_thenReturnEmailVerificationToken() {
        // given
        EmailVerificationToken emailVerificationToken = createEmailVerificationToken();
        testEntityManager.persist(emailVerificationToken);

        // when
        Optional<EmailVerificationToken> expectedToken = emailVerificationTokenRepository.findById(emailVerificationToken.getId());

        // then
        assertThat(expectedToken).isPresent();
        assertThat(expectedToken.get()).isEqualTo(emailVerificationToken);

    }

    @Test
    public void whenFindByToken_thenReturnEmailVerificationToken() {
        // given
        EmailVerificationToken emailVerificationToken = createEmailVerificationToken();
        testEntityManager.persist(emailVerificationToken);

        // when
        Optional<EmailVerificationToken> expectedToken = emailVerificationTokenRepository.findByToken(emailVerificationToken.getToken());

        // then
        assertThat(expectedToken).isPresent();
        assertThat(expectedToken.get()).isEqualTo(emailVerificationToken);
    }

    @Test
    public void whenDeleteById_thenEmailVerificationTokenDeleted() {
        // given
        EmailVerificationToken emailVerificationToken = createEmailVerificationToken();
        testEntityManager.persist(emailVerificationToken);

        // when
        emailVerificationTokenRepository.deleteById(emailVerificationToken.getId());
        EmailVerificationToken expected = testEntityManager.find(EmailVerificationToken.class, emailVerificationToken.getId());

        // then
        assertThat(expected).isNull();
    }
}