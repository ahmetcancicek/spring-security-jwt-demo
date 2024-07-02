package com.auth.demo.service;

import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.RefreshToken;
import com.auth.demo.model.User;
import com.auth.demo.repository.RefreshTokenRepository;
import com.auth.demo.util.RefreshTokenBuilder;
import com.auth.demo.util.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class RefreshTokeServiceImplTest {

    private RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenService refreshTokenService;

    private User user;
    private String token;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        refreshTokenService = new RefreshTokeServiceImpl(refreshTokenRepository);

        user = UserBuilder.generate().build();
        token = UUID.randomUUID().toString();
        refreshToken = RefreshTokenBuilder.generate()
                .withId(1L)
                .withToken(token)
                .withExpiryDate(Instant.now().minusMillis(10000))
                .withRefreshCount(0)
                .withUser(user)
                .build();
    }

    @Test
    void givenToken_whenFindByToken_thenReturnRefreshToken() {
        // given
        given(refreshTokenRepository.findByToken(any(String.class))).willReturn(Optional.ofNullable(refreshToken));

        // when
        RefreshToken expected = refreshTokenService.findByToken(token);

        // then
        verify(refreshTokenRepository, times(1)).findByToken(any(String.class));
        assertThat(expected).isEqualTo(refreshToken);
    }

    @Test
    void givenRefreshToken_whenSave_thenReturnSaveRefreshToken() {
        // given
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);

        // when
        RefreshToken expected = refreshTokenService.save(refreshToken);

        // then
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        assertThat(expected).isEqualTo(refreshToken);
    }

    @Test
    void givenExpiredRefreshToken_whenVerifyExpiration_thenThrowException() {
        // when
        Throwable throwable = catchThrowable(() -> refreshTokenService.verifyExpiration(refreshToken));

        // then
        verify(refreshTokenRepository, times(1)).deleteById(any(Long.class));
        assertThat(throwable).isInstanceOf(BusinessException.class);

    }

    @Test
    void givenRefreshTokenId_whenDeleteById_thenDeletedRefreshToken() {
        // when
        refreshTokenService.deleteById(refreshToken.getId());

        // then
        verify(refreshTokenRepository, times(1)).deleteById(refreshToken.getId());

    }

    @Test
    void givenRefreshToken_whenIncreaseCount_thenIncreasedRefreshCount() {
        // given
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);

        // when
        refreshTokenService.increaseCount(refreshToken);

        // then
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        assertThat(refreshToken.getRefreshCount()).isEqualTo(1);
    }

    @Test
    void givenUsername_whenDeleteByUsername_thenDeletedAllRefreshTokenBelongsToUser() {
        // when
        refreshTokenService.deleteByUsername(user.getUsername());

        // then
        verify(refreshTokenRepository, times(1)).deleteByUserUsername(refreshToken.getUser().getUsername());

    }

}