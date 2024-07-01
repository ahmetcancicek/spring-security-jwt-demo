package com.auth.demo.repository;

import com.auth.demo.model.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(3600000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(createUser());
        refreshToken.setRefreshCount(0);
        return refreshToken;
    }

    @Test
    public void whenSave_thenReturnSavedRefreshToken() {
        RefreshToken refreshToken = createRefreshToken();
        refreshTokenRepository.save(refreshToken);

        assertThat(refreshToken.getId()).isNotNull();
        assertThat(refreshToken.getId()).isGreaterThan(0L);
    }

    @Test
    public void whenFindById_thenReturnRefreshToken() {
        RefreshToken refreshToken = createRefreshToken();
        testEntityManager.persist(refreshToken);

        Optional<RefreshToken> expected = refreshTokenRepository.findById(refreshToken.getId());

        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(refreshToken);
    }

    @Test
    public void whenFindByToken_thenReturnRefreshToken() {
        RefreshToken refreshToken = createRefreshToken();
        testEntityManager.persist(refreshToken);

        Optional<RefreshToken> expected = refreshTokenRepository.findByToken(refreshToken.getToken());

        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(refreshToken);
    }

    @Test
    public void whenDeleteById_thenRefreshTokenDeleted() {
        RefreshToken refreshToken = createRefreshToken();
        testEntityManager.persist(refreshToken);

        refreshTokenRepository.deleteById(refreshToken.getId());
        RefreshToken expected = testEntityManager.find(RefreshToken.class, refreshToken.getId());

        assertThat(expected).isNull();
    }

    @Test
    public void whenDeleteByUserUsername_thenRefreshTokenDeleted() {
        RefreshToken refreshToken = createRefreshToken();
        testEntityManager.persist(refreshToken);

        refreshTokenRepository.deleteByUserUsername(refreshToken.getUser().getUsername());
        RefreshToken expected = testEntityManager.find(RefreshToken.class, refreshToken.getId());

        assertThat(expected).isNull();
    }
}