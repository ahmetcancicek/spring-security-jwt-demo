package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.LoginRequest;
import com.auth.demo.dto.LoginResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.model.Role;
import com.auth.demo.model.User;
import com.auth.demo.security.AuthUser;
import com.auth.demo.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private RoleService roleService;

    public AuthServiceImpl(UserConverter userConverter, PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, RoleService roleService) {
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Checks if given email already exists in the database
        // TODO: Checks if given email already exists in the database

        User user = userConverter.fromRegisterRequestToUser(registerRequest);
        user.setActive(true);
        // TODO: After the writing the publish event operation, setEmailVerified must be false
        user.setEmailVerified(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRolesForNewUser(registerRequest.registerAsAdmin()));
        User savedUser = userService.save(user);
        log.info("Created new user and saved to db: [{}]", savedUser);

        // Publish event to confirmation to user email
        // TODO: Publish event to confirmation to user email

        return userConverter.fromUserToRegisterResponse(savedUser);
    }

    private Set<Role> getRolesForNewUser(boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin)
            newUserRoles.removeIf(Role::isAdminRole);

        log.info("Setting user roles: {}", newUserRoles);
        return newUserRoles;
    }


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
        String accessToken = jwtProvider.generateToken(authentication);

        // TODO: Generate the refresh token
        return new LoginResponse(accessToken, "", "", null);
    }
}
