package com.auth.demo.controller;

import com.auth.demo.dto.LoginRequest;
import com.auth.demo.dto.LoginResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.dto.RegisterResponse;
import com.auth.demo.service.AuthService;
import com.auth.demo.util.LoginRequestBuilder;
import com.auth.demo.util.LoginResponseBuilder;
import com.auth.demo.util.RegisterRequestBuilder;
import com.auth.demo.util.RegisterResponseBuilder;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
}