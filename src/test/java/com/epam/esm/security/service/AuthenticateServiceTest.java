package com.epam.esm.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.jwt.JwtProperties;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtProperties properties;

    @InjectMocks
    AuthenticateService subject;

    @BeforeEach
    public void setup() {

    }

    @Test
    void attemptLogin() {
        fail("Should be implemented!");
    }

    @Test
    void refreshTokens() {
        fail("Should be implemented!");
    }

    @Test
    void issueAccessToken() {
        Long id = 1L;
        String secretKey = "secret";
        String name = "name";
        List<String> roles = List.of("USER");

        when(properties.getSecretKey()).thenReturn(secretKey);

        String expected = JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(Instant.now().plus(Duration.of(15, ChronoUnit.MINUTES)))
                .withClaim("name", name)
                .withClaim("authorities", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));

        String actual = subject.issueAccessToken(id, name, roles);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void issueRefreshToken() {
        Long id = 1L;
        String secretKey = "secret";

        when(properties.getSecretKey()).thenReturn(secretKey);

        String expected = JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.HOURS)))
                .sign(Algorithm.HMAC256(secretKey));

        String actual = subject.issueRefreshToken(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void loadUserById() {
        Long id = 1L;
        User user = User.builder()
                .id(id)
                .role(Role.USER)
                .build();
        UserPrincipal expected = UserPrincipal.builder().userId(id).build();

        when(userService.findById(id)).thenReturn(user);

        assertThat(subject.loadUserById(id).getUserId()).isEqualTo(expected.getUserId());

        verify(userService).findById(id);
    }
}