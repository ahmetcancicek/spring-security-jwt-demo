package com.auth.demo.service;

import com.auth.demo.dto.PasswordResetRequest;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.PasswordResetToken;
import com.auth.demo.model.User;
import com.auth.demo.repository.PasswordResetTokenRepository;
import com.auth.demo.util.PasswordResetRequestBuilder;
import com.auth.demo.util.PasswordResetTokenBuilder;
import com.auth.demo.util.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceImplTest {

    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private User user;

    private PasswordResetToken passwordResetToken;


    @BeforeEach
    void setUp() {
        // Add the Resources parameter
        ReflectionTestUtils.setField(passwordResetTokenService, "expiration", 3600000L);

        user = UserBuilder.generate().build();
        passwordResetToken = PasswordResetTokenBuilder.generate()
                .withId(1L)
                .withUser(user)
                .withActive(true)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withToken(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void givenUser_whenCreateAndSavePasswordResetToken_thenReturnToken() {
        // given
        given(passwordResetTokenRepository.save(any(PasswordResetToken.class))).willReturn(passwordResetToken);

        // when
        PasswordResetToken expected = passwordResetTokenService.createAndSavePasswordResetToken(user);

        // then
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
        assertThat(expected).isEqualTo(passwordResetToken);
    }


    @Test
    void givenExpiredToken_whenVerifyExpiration_thenThrowException() {
        // given
        passwordResetToken.setExpiryDate(Instant.now().minusMillis(10000));

        // when
        Throwable throwable = catchThrowable(() ->
                passwordResetTokenService.verifyExpiration(passwordResetToken));

        // then
        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    void givenNotActiveToken_whenVerifyExpiration_thenThrowException() {
        // given
        passwordResetToken.setActive(false);

        // when
        Throwable throwable = catchThrowable(() ->
                passwordResetTokenService.verifyExpiration(passwordResetToken));

        // then
        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    void givenNotMatchedEmail_whenMatchEmail_thenThrowException() {

        Throwable throwable = catchThrowable(() -> {
            passwordResetTokenService.matchEmail(passwordResetToken, "test@test.com");
        });

        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    void givenValidPasswordResetToken_whenGetValidToken_thenReturnToken() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestBuilder.generate()
                .withPassword(user.getPassword())
                .withConfirmPassword(user.getPassword())
                .withToken(passwordResetToken.getToken())
                .withEmail(user.getEmail())
                .build();
        given(passwordResetTokenRepository.findByToken(any(String.class))).willReturn(Optional.ofNullable(passwordResetToken));

        passwordResetTokenService.getValidToken(passwordResetRequest);

        verify(passwordResetTokenRepository, times(1)).findByToken(any(String.class));
    }

    @Test
    void givenNotActiveToken_whenGetValidToken_thenThrowException() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestBuilder.generate()
                .withPassword(user.getPassword())
                .withConfirmPassword(user.getPassword())
                .withToken(passwordResetToken.getToken())
                .withEmail(user.getEmail())
                .build();
        passwordResetToken.setActive(false);
        given(passwordResetTokenRepository.findByToken(any(String.class))).willReturn(Optional.ofNullable(passwordResetToken));

        Throwable throwable = catchThrowable(() -> {
            passwordResetTokenService.getValidToken(passwordResetRequest);
        });

        assertThat(throwable).isInstanceOf(BusinessException.class);
    }


    @Test
    void givenExpiredToken_whenGetValidToken_thenThrowException() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestBuilder.generate()
                .withPassword(user.getPassword())
                .withConfirmPassword(user.getPassword())
                .withToken(passwordResetToken.getToken())
                .withEmail(user.getEmail())
                .build();
        passwordResetToken.setExpiryDate(Instant.now().minusMillis(1000));
        given(passwordResetTokenRepository.findByToken(any(String.class))).willReturn(Optional.ofNullable(passwordResetToken));

        Throwable throwable = catchThrowable(() -> {
            passwordResetTokenService.getValidToken(passwordResetRequest);
        });

        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    void givenNotMatchedEmail_whenGetValidToken_thenThrowException() {
        PasswordResetRequest passwordResetRequest = PasswordResetRequestBuilder.generate()
                .withPassword(user.getPassword())
                .withConfirmPassword(user.getPassword())
                .withToken(passwordResetToken.getToken())
                .withEmail("test@test.com")
                .build();

        given(passwordResetTokenRepository.findByToken(any(String.class))).willReturn(Optional.ofNullable(passwordResetToken));

        Throwable throwable = catchThrowable(() -> {
            passwordResetTokenService.getValidToken(passwordResetRequest);
        });

        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

}