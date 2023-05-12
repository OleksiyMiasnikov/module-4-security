package com.epam.esm.controller;

import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.login.LoginRequest;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.service.AuthenticateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthenticateControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Mock
    AuthenticateService service;
    @InjectMocks
    AuthenticateController subject;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void login() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .name("user")
                .password("111")
                .build();
        LoginResponse response = LoginResponse.builder()
                .accessToken("- access token -")
                .refreshToken("- refresh token -")
                .build();

        String jsonLogin = "{\"name\": \"user\",\"password\": \"111\"}";

        when(service.attemptLogin(request.getName(), request.getPassword())).thenReturn(response);

        this.mockMvc.perform(get("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().stringValues("access_token", "- access token -"))
                .andExpect(header().stringValues("refresh_token", "- refresh token -"));

        verify(service).attemptLogin(request.getName(), request.getPassword());
    }

    @Test
    void secured() throws Exception {
        this.mockMvc.perform(get("/secured")
                        .with(SecurityMockMvcRequestPostProcessors
                                .user(UserPrincipal.builder()
                                        .userId(1L)
                                        .name("user_1")

                                        .build())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void refreshTokens() {
    }
}