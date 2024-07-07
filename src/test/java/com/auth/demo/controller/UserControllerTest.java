package com.auth.demo.controller;

import com.auth.demo.dto.ProfileRequest;
import com.auth.demo.dto.ProfileResponse;
import com.auth.demo.security.JwtProvider;
import com.auth.demo.security.JwtValidator;
import com.auth.demo.service.UserDetailsServiceImpl;
import com.auth.demo.service.UserService;
import com.auth.demo.util.ProfileRequestBuilder;
import com.auth.demo.util.ProfileResponseBuilder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

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
    void whenGetProfile_thenReturnUserProfile() throws Exception {
        ProfileResponse profileResponse = ProfileResponseBuilder.generate().build();

        given(userService.getProfile(any())).willReturn(profileResponse);

        mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.firstName").value(profileResponse.firstName()))
                .andExpect(jsonPath("$.data.lastName").value(profileResponse.lastName()))
                .andExpect(jsonPath("$.data.email").value(profileResponse.email()))
                .andExpect(jsonPath("$.data.username").value(profileResponse.username()))
                .andDo(print());

    }

    @Test
    void givenNewProfile_whenUpdateProfile_thenReturnNewUserProfile() throws Exception {
        ProfileRequest profileRequest = ProfileRequestBuilder.generate()
                .withFirstName("George")
                .withLastName("Smith")
                .build();

        ProfileResponse profileResponse = ProfileResponseBuilder.generate()
                .withFirstName(profileRequest.firstName())
                .withLastName(profileRequest.lastName())
                .build();

        given(userService.updateProfile(any(), any())).willReturn(profileResponse);

        mockMvc.perform(put("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value(profileResponse.firstName()))
                .andExpect(jsonPath("$.data.lastName").value(profileResponse.lastName()));
    }
}