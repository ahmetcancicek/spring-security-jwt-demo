package com.auth.demo.service;

import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.TokenStatus;
import com.auth.demo.model.User;
import com.auth.demo.repository.EmailVerificationTokenRepository;
import com.auth.demo.util.EmailVerificationTokenBuilder;
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
class EmailVerificationTokenServiceImplTest {

    @InjectMocks
    private EmailVerificationTokenServiceImpl emailVerificationTokenService;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    private EmailVerificationToken emailVerificationToken;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        // Add the Resources parameter
        ReflectionTestUtils.setField(emailVerificationTokenService, "emailVerificationTokenExpiryDuration", 3600000L);

        user = UserBuilder.generate().build();
        token = UUID.randomUUID().toString();

        emailVerificationToken = EmailVerificationTokenBuilder.generate()
                .withId(1L)
                .withToken(token)
                .withExpiryDate(Instant.now().plusMillis(10000))
                .withUser(user)
                .withTokenStatus(TokenStatus.STATUS_PENDING)
                .build();

    }

    @Test
    void givenUserAndToken_whenSaveVerificationToken_thenReturnToken() {
        // given
        given(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);

        // when
        EmailVerificationToken expected = emailVerificationTokenService.saveVerificationToken(user, token);

        // then
        verify(emailVerificationTokenRepository, times(1)).save(any(EmailVerificationToken.class));
        assertThat(expected).isEqualTo(emailVerificationToken);
    }

    @Test
    void givenUserAndToken_whenCreateAndSaveVerificationToken_thenReturnToken() {
        // given
        given(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);

        // when
        EmailVerificationToken expected = emailVerificationTokenService.createAndSaveVerificationToken(user);

        // then
        verify(emailVerificationTokenRepository, times(1)).save(any(EmailVerificationToken.class));
        assertThat(expected).isEqualTo(emailVerificationToken);
    }

    @Test
    void givenToken_whenFindByToken_thenReturnToken() {
        // given
        given(emailVerificationTokenRepository.findByToken(any(String.class))).willReturn(Optional.ofNullable(emailVerificationToken));

        // when
        EmailVerificationToken expected = emailVerificationTokenService.findByToken(token);

        // then
        verify(emailVerificationTokenRepository, times(1)).findByToken(any(String.class));
        assertThat(expected).isEqualTo(emailVerificationToken);
    }

    @Test
    void givenExistingToken_whenUpdateExistingToken_thenReturnToken() {
        // given
        given(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);

        // when
        EmailVerificationToken expected = emailVerificationTokenService.updateExistingToken(emailVerificationToken);

        // then
        verify(emailVerificationTokenRepository, times(1)).save(any(EmailVerificationToken.class));
        assertThat(expected).isEqualTo(emailVerificationToken);
    }

    @Test
    void givenEmailVerificationToken_whenSave_thenReturnToken() {
        given(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);

        EmailVerificationToken expected = emailVerificationTokenService.save(emailVerificationToken);

        verify(emailVerificationTokenRepository, times(1)).save(any(EmailVerificationToken.class));
        assertThat(expected).isEqualTo(emailVerificationToken);
    }

    @Test
    void givenExpiredEmailVerificationToken_whenVerifyExpiration_thenThrowException() {
        emailVerificationToken.setExpiryDate(Instant.now().minusSeconds(10));

        Throwable throwable = catchThrowable(() -> emailVerificationTokenService.verifyExpiration(emailVerificationToken));

        assertThat(throwable).isInstanceOf(BusinessException.class);

    }
}