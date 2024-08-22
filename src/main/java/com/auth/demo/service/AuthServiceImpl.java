package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.*;
import com.auth.demo.event.PasswordResetCompletedEvent;
import com.auth.demo.event.PasswordResetLinkEvent;
import com.auth.demo.event.UserRegistrationEvent;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.*;
import com.auth.demo.security.AuthUser;
import com.auth.demo.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${app.mail.confirm}")
    private String verificationURL;

    @Value("${app.mail.password.resetlink}")
    private String passwordResetURL;

    public AuthServiceImpl(UserConverter userConverter, PasswordEncoder passwordEncoder, UserService userService, PasswordResetTokenService passwordResetTokenService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, RoleService roleService, RefreshTokenService refreshTokenService, EmailVerificationTokenService emailVerificationTokenService, ApplicationEventPublisher applicationEventPublisher) {
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Checks if given email already exists in the database
        checkEmailOrUsernameAlreadyExists(registerRequest.email(), registerRequest.username());

        // Create and save user account
        User savedUser = createUser(registerRequest);

        // Generate email verification token and publish
        createEmailVerificationTokenAndPublish(savedUser);

        // Return the response
        return userConverter.fromUserToRegisterResponse(savedUser);
    }

    private void createEmailVerificationTokenAndPublish(User savedUser) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.createAndSaveVerificationToken(savedUser);
        String token = emailVerificationToken.getToken();
        String URL = createVerificationURL(token);
        // Publish event to email user for email confirmation
        UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(savedUser, URL);
        applicationEventPublisher.publishEvent(userRegistrationEvent);
    }

    private String createVerificationURL(String token) {
        StringBuilder url;
        url = new StringBuilder(verificationURL);
        url.append("?token=").append(token);
        return url.toString();
    }

    private User createUser(RegisterRequest registerRequest) {
        User user = userConverter.fromRegisterRequestToUser(registerRequest);
        user.setActive(true);
        user.setEmailVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRolesForNewUser(registerRequest.registerAsAdmin()));
        return userService.save(user);
    }

    private void checkEmailOrUsernameAlreadyExists(String email, String username) {
        if (isExistsByEmail(email)) {
            log.error("Email already exists: [{}]", email);
            throw new BusinessException("user.emailAlreadyExists");
        }

        if (isExistsByUsername(username)) {
            log.error("Email already exists: [{}]", username);
            throw new BusinessException("user.usernameAlreadyExists");
        }
    }

    private Set<Role> getRolesForNewUser(boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin)
            newUserRoles.removeIf(Role::isAdminRole);

        log.info("Setting user roles: {}", newUserRoles);
        return newUserRoles;
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return userService.existsByUsername(username);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return userService.existsByEmail(email);
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()));

        // Add authentication to context
        AuthUser authenticatedUser = (AuthUser) authentication.getPrincipal();
        log.info("Logged in user returned: [{}]", authenticatedUser.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate access token
        String accessToken = jwtProvider.generateToken(authenticatedUser);
        log.info("Generated access token for: [{}]", authenticatedUser.getUsername());

        // Generate refresh token
        String refreshToken = createAndSaveRefreshToken(authenticatedUser).getToken();
        log.info("Generated refresh token for: [{}]", authenticatedUser.getUsername());

        return new LoginResponse(accessToken, refreshToken, "Bearer", jwtProvider.getExpiryDuration());
    }

    @Override
    public RefreshToken createAndSaveRefreshToken(AuthUser authUser) {
        User user = authUser.getUser();

        // Deleted old refresh token
        refreshTokenService.deleteByUsername(user.getUsername());

        // Create new refresh token
        RefreshToken refreshToken = refreshTokenService.generateToken();
        refreshToken.setUser(user);
        refreshTokenService.save(refreshToken);

        return refreshToken;
    }

    @Override
    public LoginResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        String refreshToken = tokenRefreshRequest.refreshToken();

        // Find refresh token
        RefreshToken existedRefreshToken = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.verifyExpiration(existedRefreshToken);
        refreshTokenService.increaseCount(existedRefreshToken);

        // Generate access token
        AuthUser authUser = new AuthUser(existedRefreshToken.getUser());
        String accessToken = jwtProvider.generateToken(authUser);

        return new LoginResponse(accessToken, refreshToken, "Bearer", jwtProvider.getExpiryDuration());
    }

    @Override
    public ConfirmResponse confirmEmailRegistration(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(token);
        User user = emailVerificationToken.getUser();

        // Check if email verification token has already verified
        if (user.getEmailVerified()) {
            log.info("Email verification token already verified for: [{}]", user.getUsername());
            return new ConfirmResponse(user.getUsername(), user.getEmail(), user.getEmailVerified());
        }

        // Verify and update the existing verification token
        emailVerificationTokenService.verifyExpiration(emailVerificationToken);
        emailVerificationToken.setConfirmedStatus();
        emailVerificationTokenService.save(emailVerificationToken);

        // Mark user's token as confirmed
        user.markVerificationConfirmed();
        userService.save(user);


        log.info("Email verification token confirmed: [{}]", user.getUsername());
        return new ConfirmResponse(user.getUsername(), user.getEmail(), user.getEmailVerified());
    }

    @Override
    public EmailVerificationToken recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken);

        if (emailVerificationToken.getUser().getEmailVerified()) {
            throw new BusinessException("user.alreadyConfirmed");
        }

        return emailVerificationTokenService.updateExistingToken(emailVerificationToken);
    }


    @Override
    public void resendRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = recreateRegistrationToken(existingToken);
        User user = emailVerificationToken.getUser();
        String newToken = emailVerificationToken.getToken();
        String URL = createVerificationURL(newToken);

        // Publish event to email user for email confirmation
        UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(user, URL);
        applicationEventPublisher.publishEvent(userRegistrationEvent);
    }

    @Transactional
    @Override
    public void resetPassword(PasswordResetRequest resetRequest) {
        // Check if the password reset token is valid
        PasswordResetToken passwordResetToken = passwordResetTokenService.getValidToken(resetRequest);

        // Claim the password reset token
        passwordResetTokenService.claimToken(passwordResetToken);

        // Encode new password
        final String encodedPassword = passwordEncoder.encode(resetRequest.confirmPassword());
        // Update user
        User user = passwordResetToken.getUser();
        user.setPassword(encodedPassword);
        userService.save(user);

        // Publish an event for successful message
        PasswordResetCompletedEvent passwordResetCompletedEvent = new PasswordResetCompletedEvent(user);
        applicationEventPublisher.publishEvent(passwordResetCompletedEvent);
    }

    public void resetLink(PasswordResetLinkRequest linkRequest) {
        // Get an email from request
        String email = linkRequest.email();

        // Get user and create token for verification
        User user = userService.findByEmail(email);
        String name = user.getFirstName();
        PasswordResetToken passwordResetToken = passwordResetTokenService.createAndSavePasswordResetToken(user);

        // Create link
        StringBuilder url = new StringBuilder(passwordResetURL);
        url.append("?token").append("=").append(passwordResetToken.getToken());

        // Publish an event for sending reset link to the user
        PasswordResetLinkEvent passwordResetLinkEvent = new PasswordResetLinkEvent(user, url.toString());
        applicationEventPublisher.publishEvent(passwordResetLinkEvent);
    }
}
