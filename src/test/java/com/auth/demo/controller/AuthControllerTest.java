package com.auth.demo.controller;

import com.auth.demo.dto.*;
import com.auth.demo.security.JwtAuthenticationFilter;
import com.auth.demo.security.JwtProvider;
import com.auth.demo.security.JwtValidator;
import com.auth.demo.service.AuthService;
import com.auth.demo.service.UserDetailsServiceImpl;
import com.auth.demo.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtValidator jwtValidator;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenUniqueUsernameAndUniqueEmail_whenRegisterUser_thenReturnRegisteredUser() throws Exception {
        RegisterRequest registerRequest = RegisterRequestBuilder.generate().build();
        RegisterResponse registerResponse = RegisterResponseBuilder.generate().build();

        given(authService.registerUser(any(RegisterRequest.class))).willReturn(registerResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.firstName").value(registerResponse.firstName()))
                .andExpect(jsonPath("$.data.lastName").value(registerResponse.lastName()))
                .andExpect(jsonPath("$.data.email").value(registerResponse.email()))
                .andExpect(jsonPath("$.data.username").value(registerResponse.username()))
                .andExpect(jsonPath("$.data.emailVerified").value(registerResponse.emailVerified()))
                .andExpect(jsonPath("$.data.active").value(registerResponse.active()))
                .andDo(print());
    }

    @Test
    void givenValidCredentials_whenLogin_thenReturnToken() throws Exception {
        LoginRequest loginRequest = LoginRequestBuilder.generate().build();
        LoginResponse loginResponse = LoginResponseBuilder.generate().build();

        given(authService.login(any(LoginRequest.class))).willReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.accessToken").value(loginResponse.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(loginResponse.refreshToken()))
                .andDo(print());
    }

    @Test
    void givenValidRefreshToken_whenRefreshToken_thenReturnNewAccessToken() throws Exception {
        String refreshToken = UUID.randomUUID().toString();
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequestBuilder
                .generate()
                .withRefreshToken(refreshToken)
                .build();
        LoginResponse loginResponse = LoginResponseBuilder.generate().build();

        given(authService.refreshToken(tokenRefreshRequest)).willReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tokenRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.accessToken").value(loginResponse.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(loginResponse.refreshToken()))
                .andDo(print());
    }

    @Test
    void givenValidEmailVerificationToken_whenConfirmRegistration_thenReturnConfirmResponse() throws Exception {
        String token = UUID.randomUUID().toString();
        ConfirmResponse confirmResponse = ConfirmResponseBuilder
                .generate()
                .withEmail("johndoor@email.com")
                .withUsername("johndoor")
                .withEmailVerified(true)
                .build();

        given(authService.confirmEmailRegistration(any(String.class))).willReturn(confirmResponse);

        mockMvc.perform(get("/api/v1/auth/confirm?token={token}", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(confirmResponse.email()))
                .andExpect(jsonPath("$.data.username").value(confirmResponse.username()))
                .andExpect(jsonPath("$.data.emailVerified").value(confirmResponse.emailVerified()));
    }

    @Test
    void givenIsExistsEmail_whenCheckEmailInUse_thenReturnTrue() throws Exception {
        String email = "johndoor@email.com";
        EmailCheckResponse emailCheckResponse = new EmailCheckResponse(true, email);

        given(authService.isExistsByEmail(any(String.class))).willReturn(true);

        mockMvc.perform(get("/api/v1/auth/checkEmailInUse?email={email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.isEmailInUse").value(true));
    }

    @Test
    void givenIsExistsUsername_whenCheckUsername_thenReturnTrue() throws Exception {
        String username = "johndoor";
        UsernameCheckResponse usernameCheckResponse = new UsernameCheckResponse(true, username);

        given(authService.isExistsByUsername(any(String.class))).willReturn(true);

        mockMvc.perform(get("/api/v1/auth/checkUsernameInUse?username={username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.isUsernameInUse").value(true));
    }

}