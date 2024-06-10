package com.auth.demo.repository;

import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.PasswordResetToken;
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

class PasswordResetTokenRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    private PasswordResetToken createPasswordResetToken() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(createUser());
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(3600000));
        passwordResetToken.setActive(true);
        passwordResetToken.setClaimed(false);
        return passwordResetToken;
    }

    @Test
    public void whenSave_thenReturnSavedPasswordResetToken() {
        PasswordResetToken passwordResetToken = createPasswordResetToken();
        passwordResetTokenRepository.save(passwordResetToken);

        assertThat(passwordResetToken.getId()).isNotNull();
        assertThat(passwordResetToken.getId()).isGreaterThan(0L);
    }

    @Test
    public void whenFindById_thenReturnPasswordResetToken() {
        PasswordResetToken passwordResetToken = createPasswordResetToken();
        testEntityManager.persist(passwordResetToken);

        Optional<PasswordResetToken> expected = passwordResetTokenRepository.findById(passwordResetToken.getId());

        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(passwordResetToken);
    }

    @Test
    public void whenFindByToken_thenReturnPasswordResetToken() {
        PasswordResetToken passwordResetToken = createPasswordResetToken();
        testEntityManager.persist(passwordResetToken);

        Optional<PasswordResetToken> expected = passwordResetTokenRepository.findByToken(passwordResetToken.getToken());

        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(passwordResetToken);
    }

    @Test
    public void whenDeleteById_thenPasswordResetTokenDeleted() {
        PasswordResetToken passwordResetToken = createPasswordResetToken();
        testEntityManager.persist(passwordResetToken);

        passwordResetTokenRepository.deleteById(passwordResetToken.getId());
        PasswordResetToken expected = testEntityManager.find(PasswordResetToken.class, passwordResetToken.getId());


        assertThat(expected).isNull();
    }
}