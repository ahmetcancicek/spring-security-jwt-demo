package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.model.User;
import com.auth.demo.security.JwtProvider;
import com.auth.demo.util.RegisterRequestBuilder;
import com.auth.demo.util.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private AuthServiceImpl authService;

    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private RoleService roleService;

    private AuthenticationManager authenticationManager;

    private JwtProvider jwtProvider;

    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = Mockito.mock(UserService.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtProvider = Mockito.mock(JwtProvider.class);
        roleService = Mockito.mock(RoleService.class);
        userConverter = new UserConverter();
        authService = new AuthServiceImpl(userConverter, passwordEncoder, userService, authenticationManager, jwtProvider, roleService);
    }

    @Test
    void givenUniqueUsernameAndUniqueEmail_whenRegisterUser_thenReturnRegisteredUser() {
        // given
        User user = UserBuilder.generate().build();
        RegisterRequest registerRequest = RegisterRequestBuilder
                .generate()
                .withEmail(user.getEmail())
                .withUsername(user.getUsername())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withPassword(user.getPassword())
                .build();
        given(userService.save(any(User.class))).willReturn(user);

        // when
        RegisterResponse expected = authService.registerUser(registerRequest);

        // then
        assertThat(expected.username()).isEqualTo(registerRequest.username());
        assertThat(expected.email()).isEqualTo(registerRequest.email());
        assertThat(expected.firstName()).isEqualTo(registerRequest.firstName());
        assertThat(expected.lastName()).isEqualTo(registerRequest.lastName());
    }

    @Test
    void givenExistingEmail_whenRegisterUser_thenThrowException() {

    }

    @Test
    void givenExistingUsername_whenRegisterUser_thenThrowException() {

    }


    @Test
    void givenValidCredentials_whenLogin_thenReturnToken() {

    }
}