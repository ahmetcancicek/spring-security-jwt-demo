package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.*;
import com.auth.demo.event.UserRegistrationEvent;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.RefreshToken;
import com.auth.demo.model.Role;
import com.auth.demo.model.User;
import com.auth.demo.security.AuthUser;
import com.auth.demo.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher applicationEventPublisher;


    public AuthServiceImpl(UserConverter userConverter, PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, RoleService roleService, RefreshTokenService refreshTokenService, EmailVerificationTokenService emailVerificationTokenService, ApplicationEventPublisher applicationEventPublisher) {
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
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

        User user = userConverter.fromRegisterRequestToUser(registerRequest);
        user.setActive(true);
        user.setEmailVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRolesForNewUser(registerRequest.registerAsAdmin()));
        User savedUser = userService.save(user);
        log.info("Created new user and saved to db: [{}]", savedUser);


        // Generate email verification token
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.createAndSaveVerificationToken(user);
        String token = emailVerificationToken.getToken();
        // Publish event to email user for email confirmation
        UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(user, token);
        applicationEventPublisher.publishEvent(userRegistrationEvent);

        return userConverter.fromUserToRegisterResponse(savedUser);
    }

    private void checkEmailOrUsernameAlreadyExists(String email, String username) {
        if (isExistsByEmail(email)) {
            log.error("Email already exists: [{}]", email);
            throw new BusinessException("user.emailAlreadyExists: [{}]", email);
        }

        if (isExistsByUsername(username)) {
            log.error("Email already exists: [{}]", username);
            throw new BusinessException("user.usernameAlreadyExists: [{}]", username);
        }
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return userService.existsByUsername(username);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return userService.existsByEmail(email);
    }

    private Set<Role> getRolesForNewUser(boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin)
            newUserRoles.removeIf(Role::isAdminRole);

        log.info("Setting user roles: {}", newUserRoles);
        return newUserRoles;
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

    @Transactional
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

        AuthUser authUser = new AuthUser(emailVerificationToken.getUser());
        User user = authUser.getUser();
        if (authUser.getEmailVerified()) {
            log.info("Email verification token already exists for: [{}]", authUser.getUsername());
            new ConfirmResponse(user.getUsername(), user.getEmail(), user.getEmailVerified());
        }

        emailVerificationTokenService.verifyExpiration(emailVerificationToken);
        emailVerificationToken.setConfirmedStatus();
        emailVerificationTokenService.save(emailVerificationToken);

        user.markVerificationConfirmed();
        userService.save(user);
        log.info("Email verification token confirmed: [{}]", authUser.getUsername());

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

}
