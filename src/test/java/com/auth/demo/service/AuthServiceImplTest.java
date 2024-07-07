package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.*;
import com.auth.demo.event.UserRegistrationEvent;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.*;
import com.auth.demo.security.AuthUser;
import com.auth.demo.security.JwtProvider;
import com.auth.demo.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private AuthServiceImpl authService;

    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private EmailVerificationTokenService emailVerificationTokenService;

    private RoleService roleService;

    private RefreshTokenService refreshTokenService;

    private PasswordResetTokenService passwordResetTokenService;

    private AuthenticationManager authenticationManager;

    private JwtProvider jwtProvider;

    private UserConverter userConverter;

    private ApplicationEventPublisher applicationEventPublisher;

    private User user;
    private EmailVerificationToken emailVerificationToken;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = Mockito.mock(UserService.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtProvider = Mockito.mock(JwtProvider.class);
        roleService = Mockito.mock(RoleService.class);
        emailVerificationTokenService = Mockito.mock(EmailVerificationTokenService.class);
        refreshTokenService = Mockito.mock(RefreshTokenService.class);
        passwordResetTokenService = Mockito.mock(PasswordResetTokenService.class);
        userConverter = new UserConverter();
        applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        authService = new AuthServiceImpl(userConverter, passwordEncoder, userService, passwordResetTokenService, authenticationManager, jwtProvider, roleService, refreshTokenService, emailVerificationTokenService, applicationEventPublisher);


        user = UserBuilder.generate().build();
        emailVerificationToken = EmailVerificationTokenBuilder
                .generate()
                .withTokenStatus(TokenStatus.STATUS_PENDING)
                .withToken(UUID.randomUUID().toString())
                .withUser(user)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withId(1L)
                .build();

        registerRequest = RegisterRequestBuilder
                .generate()
                .withEmail(user.getEmail())
                .withUsername(user.getUsername())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withPassword(user.getPassword())
                .build();
    }

    @Test
    void givenUniqueUsernameAndUniqueEmail_whenRegisterUser_thenReturnRegisteredUser() {
        // given
        given(userService.existsByUsername(any(String.class))).willReturn(false);
        given(userService.existsByEmail(any(String.class))).willReturn(false);
        given(userService.save(any(User.class))).willReturn(user);
        given(emailVerificationTokenService.createAndSaveVerificationToken(any(User.class))).willReturn(emailVerificationToken);

        // when
        RegisterResponse expected = authService.registerUser(registerRequest);

        // then
        verify(userService, times(1)).existsByUsername(any(String.class));
        verify(userService, times(1)).existsByEmail(any(String.class));
        verify(userService, times(1)).save(any(User.class));
        verify(emailVerificationTokenService, times(1)).createAndSaveVerificationToken(any(User.class));
        assertThat(expected.username()).isEqualTo(registerRequest.username());
        assertThat(expected.email()).isEqualTo(registerRequest.email());
        assertThat(expected.firstName()).isEqualTo(registerRequest.firstName());
        assertThat(expected.lastName()).isEqualTo(registerRequest.lastName());
        assertThat(expected.active()).isEqualTo(true);
    }

    @Test
    void givenExistingEmail_whenRegisterUser_thenThrowException() {
        // given
        given(userService.existsByEmail(any(String.class))).willReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> {
            authService.registerUser(registerRequest);
        });

        // then
        assertThat(throwable).isInstanceOf(BusinessException.class);
        verify(userService, times(1)).existsByEmail(any(String.class));

    }

    @Test
    void givenExistingUsername_whenRegisterUser_thenThrowException() {
        // given
        given(userService.existsByEmail(any(String.class))).willReturn(false);
        given(userService.existsByUsername(any(String.class))).willReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> {
            authService.registerUser(registerRequest);
        });

        // then
        assertThat(throwable).isInstanceOf(BusinessException.class);
        verify(userService, times(1)).existsByEmail(any(String.class));
        verify(userService, times(1)).existsByUsername(any(String.class));
    }

    @Test
    void givenExistingUsername_whenIsExistsByUsername_thenReturnTrue() {
        // given
        given(userService.existsByUsername(any(String.class))).willReturn(true);

        // when
        boolean result = authService.isExistsByUsername(user.getUsername());

        // then
        assertThat(result).isTrue();
        verify(userService, times(1)).existsByUsername(any(String.class));
    }

    @Test
    void givenExistingEmail_whenIsExistsByEmail_thenReturnTrue() {
        // given
        given(userService.existsByEmail(any(String.class))).willReturn(true);

        boolean result = authService.isExistsByEmail(user.getEmail());

        assertThat(result).isTrue();
        verify(userService, times(1)).existsByEmail(any(String.class));
    }

    @Test
    void givenValidCredentials_whenLogin_thenReturnToken() {
        LoginRequest loginRequest = LoginRequestBuilder.generate().build();
        String aToken = UUID.randomUUID().toString();
        String rToken = UUID.randomUUID().toString();
        Authentication authentication = Mockito.mock(Authentication.class);
        AuthUser authUser = new AuthUser(user);
        RefreshToken refreshToken = RefreshTokenBuilder.generate()
                .withId(1L)
                .withRefreshCount(0)
                .withUser(user)
                .withToken(rToken)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .build();

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(authUser);
        given(jwtProvider.generateToken(any(AuthUser.class))).willReturn(aToken);
        given(refreshTokenService.generateToken()).willReturn(refreshToken);
        given(refreshTokenService.save(any(RefreshToken.class))).willReturn(refreshToken);


        LoginResponse loginResponse = authService.login(loginRequest);


        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider).generateToken(any(AuthUser.class));
        verify(refreshTokenService).generateToken();
        verify(refreshTokenService).save(any(RefreshToken.class));

        assertThat(loginResponse.accessToken()).isEqualTo(aToken);
        assertThat(loginResponse.refreshToken()).isEqualTo(rToken);
        assertThat(loginResponse.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void givenAuthenticated_whenCreateAndSaveRefreshToken_thenReturnRefreshToken() {
        AuthUser authUser = Mockito.mock(AuthUser.class);
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshTokenBuilder.generate()
                .withId(1L)
                .withRefreshCount(0)
                .withUser(user)
                .withToken(token)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .build();


        given(authUser.getUser()).willReturn(user);
        given(refreshTokenService.generateToken()).willReturn(refreshToken);
        given(refreshTokenService.save(any(RefreshToken.class))).willReturn(refreshToken);


        RefreshToken savedRefreshToken = authService.createAndSaveRefreshToken(authUser);


        verify(refreshTokenService).save(any(RefreshToken.class));
        assertThat(savedRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    void givenValidRefreshToken_whenRefreshToken_thenReturnNewAccessToken() {
        String refreshToken = UUID.randomUUID().toString();
        String accessToken = UUID.randomUUID().toString();
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequestBuilder
                .generate()
                .withRefreshToken(refreshToken)
                .build();
        RefreshToken dummyRefreshToken = RefreshTokenBuilder.generate()
                .withId(1L)
                .withRefreshCount(0)
                .withUser(user)
                .withToken(refreshToken)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .build();

        given(refreshTokenService.findByToken(any(String.class))).willReturn(dummyRefreshToken);
        given(jwtProvider.generateToken(any(AuthUser.class))).willReturn(accessToken);


        LoginResponse loginResponse = authService.refreshToken(tokenRefreshRequest);


        verify(refreshTokenService).findByToken(any(String.class));
        verify(jwtProvider).generateToken(any(AuthUser.class));
        assertThat(loginResponse.accessToken()).isEqualTo(accessToken);
        assertThat(loginResponse.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void givenValidEmailVerificationToken_whenConfirmEmailRegistration_thenReturnConfirmResponse() {
        user = UserBuilder.generate()
                .withEmailVerified(false)
                .build();
        emailVerificationToken = EmailVerificationTokenBuilder
                .generate()
                .withTokenStatus(TokenStatus.STATUS_PENDING)
                .withToken(UUID.randomUUID().toString())
                .withUser(user)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withId(1L)
                .build();
        given(emailVerificationTokenService.findByToken(any(String.class))).willReturn(emailVerificationToken);
        given(emailVerificationTokenService.save(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);
        given(userService.save(any(User.class))).willReturn(user);


        ConfirmResponse confirmResponse = authService.confirmEmailRegistration(emailVerificationToken.getToken());


        verify(emailVerificationTokenService).findByToken(any(String.class));
        verify(emailVerificationTokenService).save(any(EmailVerificationToken.class));
        verify(userService).save(any(User.class));
        assertThat(confirmResponse.username()).isEqualTo(user.getUsername());
        assertThat(confirmResponse.email()).isEqualTo(user.getEmail());
        assertThat(confirmResponse.emailVerified()).isEqualTo(user.getEmailVerified());
    }


    @Test
    void givenAlreadyVerifiedUser_whenConfirmEmailRegistration_thenReturnConfirmResponse() {
        user = UserBuilder.generate()
                .withEmailVerified(true)
                .build();
        given(emailVerificationTokenService.findByToken(any(String.class))).willReturn(emailVerificationToken);


        ConfirmResponse confirmResponse = authService.confirmEmailRegistration(emailVerificationToken.getToken());


        verify(emailVerificationTokenService).findByToken(any(String.class));
        assertThat(confirmResponse.username()).isEqualTo(user.getUsername());
        assertThat(confirmResponse.email()).isEqualTo(user.getEmail());
        assertThat(confirmResponse.emailVerified()).isEqualTo(user.getEmailVerified());
    }

    @Test
    void givenAlreadyVerifiedEmailVerificationToken_whenConfirmEmailRegistration_thenThrowException() {
        user = UserBuilder.generate()
                .withEmailVerified(false)
                .build();
        emailVerificationToken = EmailVerificationTokenBuilder
                .generate()
                .withTokenStatus(TokenStatus.STATUS_CONFIRMED)
                .withToken(UUID.randomUUID().toString())
                .withUser(user)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withId(1L)
                .build();
        given(emailVerificationTokenService.findByToken(any(String.class))).willReturn(emailVerificationToken);
        doThrow(new BusinessException("")).when(emailVerificationTokenService).verifyExpiration(any(EmailVerificationToken.class));


        Throwable throwable = catchThrowable(() -> {
            authService.confirmEmailRegistration(emailVerificationToken.getToken());
        });


        verify(emailVerificationTokenService).findByToken(any(String.class));
        verify(emailVerificationTokenService).verifyExpiration(any(EmailVerificationToken.class));
        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    void givenExistingEmailVerificationToken_whenRecreateRegistrationToken_thenReturnAsUpdatedExistingEmailVerificationToken() {
        user = UserBuilder.generate()
                .withEmailVerified(false)
                .build();
        emailVerificationToken = EmailVerificationTokenBuilder
                .generate()
                .withTokenStatus(TokenStatus.STATUS_PENDING)
                .withToken(UUID.randomUUID().toString())
                .withUser(user)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withId(1L)
                .build();

        given(emailVerificationTokenService.findByToken(any(String.class))).willReturn(emailVerificationToken);
        given(emailVerificationTokenService.updateExistingToken(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);

        authService.recreateRegistrationToken(emailVerificationToken.getToken());


        verify(emailVerificationTokenService).findByToken(any(String.class));
        verify(emailVerificationTokenService).updateExistingToken(any(EmailVerificationToken.class));

    }

    @Test
    void givenExistingEmailVerificationTokenAsUserAlreadyConfirmed_whenRecreateRegistrationToken_thenThrowException() {
        user = UserBuilder.generate()
                .withEmailVerified(true)
                .build();
        emailVerificationToken = EmailVerificationTokenBuilder
                .generate()
                .withTokenStatus(TokenStatus.STATUS_CONFIRMED)
                .withToken(UUID.randomUUID().toString())
                .withUser(user)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withId(1L)
                .build();

        given(emailVerificationTokenService.findByToken(any(String.class))).willReturn(emailVerificationToken);

        Throwable throwable = catchThrowable(() -> {
            authService.recreateRegistrationToken(emailVerificationToken.getToken());
        });


        verify(emailVerificationTokenService).findByToken(any(String.class));
        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    void givenExistingEmailVerificationToken_whenResendRegistrationToken_thenResendRegistrationToken() {
        user = UserBuilder.generate()
                .withEmailVerified(false)
                .build();
        emailVerificationToken = EmailVerificationTokenBuilder
                .generate()
                .withTokenStatus(TokenStatus.STATUS_PENDING)
                .withToken(UUID.randomUUID().toString())
                .withUser(user)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withId(1L)
                .build();

        given(emailVerificationTokenService.findByToken(any(String.class))).willReturn(emailVerificationToken);
        given(emailVerificationTokenService.updateExistingToken(any(EmailVerificationToken.class))).willReturn(emailVerificationToken);

        authService.resendRegistrationToken(emailVerificationToken.getToken());


        verify(emailVerificationTokenService).findByToken(any(String.class));
        verify(emailVerificationTokenService).updateExistingToken(any(EmailVerificationToken.class));
        verify(applicationEventPublisher).publishEvent(any(UserRegistrationEvent.class));
    }

    @Test
    void givenPasswordRequest_whenResetPassword_thenRestPassword() {
        PasswordResetToken passwordResetToken = PasswordResetTokenBuilder.generate()
                .withId(1L)
                .withUser(user)
                .withActive(true)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withToken(UUID.randomUUID().toString())
                .build();

        PasswordResetRequest passwordResetRequest = PasswordResetRequestBuilder.generate()
                .withPassword(user.getPassword())
                .withConfirmPassword(user.getPassword())
                .withEmail(user.getEmail())
                .withToken(passwordResetToken.getToken())
                .build();

        given(passwordResetTokenService.getValidToken(any(PasswordResetRequest.class))).willReturn(passwordResetToken);
        given(passwordEncoder.encode(any())).willReturn("encodedPassword");


        authService.resetPassword(passwordResetRequest);


        verify(passwordResetTokenService).claimToken(passwordResetToken);
        verify(userService).save(any(User.class));
        verify(applicationEventPublisher).publishEvent(any());
    }

    @Test
    void givenPasswordResetLinkRequest_whenResetLink_thenSendResetLink() {
        PasswordResetLinkRequest resetRequest = new PasswordResetLinkRequest(user.getEmail());
        PasswordResetToken passwordResetToken = PasswordResetTokenBuilder.generate()
                .withId(1L)
                .withUser(user)
                .withActive(true)
                .withExpiryDate(Instant.now().plusMillis(3600000L))
                .withToken(UUID.randomUUID().toString())
                .build();

        given(userService.findByEmail(any(String.class))).willReturn(user);
        given(passwordResetTokenService.createAndSavePasswordResetToken(any(User.class))).willReturn(passwordResetToken);


        authService.resetLink(resetRequest);


        verify(userService).findByEmail(any(String.class));
        verify(passwordResetTokenService).createAndSavePasswordResetToken(any(User.class));
        verify(applicationEventPublisher).publishEvent(any());
    }
}