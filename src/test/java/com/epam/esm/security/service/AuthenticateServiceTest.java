package com.epam.esm.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.exception.RefreshTokenFailException;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.security.RefreshTokenStorage;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.UserPrincipalAuthenticationToken;
import com.epam.esm.security.jwt.JwtDecoder;
import com.epam.esm.security.jwt.JwtPrincipalConverter;
import com.epam.esm.security.jwt.JwtProperties;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtProperties properties;
    @Mock
    private JwtDecoder decoder;
    @Mock
    private JwtPrincipalConverter converter;
    @Mock
    private RefreshTokenStorage storage;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    AuthenticateService subject;

    private final String secretKey = "secret";
    private Long id;
    private String name;
    private String password;
    private UserPrincipal principal;
    private LoginResponse expected;
    private String authorization;
    private User user;
    private DecodedJWT decodedJWT;

    @BeforeEach
    public void setup() {
        id = 1L;
        name = "name";
        password = "password";
        authorization = "Bearer ~refresh token~";

        List<SimpleGrantedAuthority> roles =
                List.of(new SimpleGrantedAuthority(Role.USER.name()));

        user = User.builder()
                .id(id)
                .name(name)
                .role(Role.USER)
                .build();

        principal = UserPrincipal.builder()
                .userId(id)
                .name(name)
                .password(password)
                .authorities(roles)
                .build();

        String newAccessToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(Instant.now().plus(Duration.of(15, ChronoUnit.MINUTES)))
                .withClaim("name", name)
                .withClaim("authorities", List.of(Role.USER.name()))
                .sign(Algorithm.HMAC256(secretKey));

        String newRefreshToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.HOURS)))
                .sign(Algorithm.HMAC256(secretKey));

        expected = LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        decodedJWT = JWT.decode(JWT.create().sign(Algorithm.HMAC256(secretKey)));

    }

    @Test
    void attemptLogin() {
        Authentication authentication = new UserPrincipalAuthenticationToken(principal);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(properties.getSecretKey()).thenReturn(secretKey);

        LoginResponse actual = subject.attemptLogin(name, password);

        assertThat(actual.getRefreshToken()).isEqualTo(expected.getRefreshToken());
        assertThat(actual.getAccessToken()).isEqualTo(expected.getAccessToken());

        verify(authenticationManager).authenticate(any());
        verify(properties, times(2)).getSecretKey();
    }

    @Test
    void refreshTokens() {
        when(converter.convert(decodedJWT)).thenReturn(principal);
        when(decoder.decode(anyString())).thenReturn(decodedJWT);
        when(storage.get(principal.getUserId()))
                .thenReturn(authorization.substring("Bearer ".length()));
        when(userService.findById(id)).thenReturn(user);
        when(properties.getSecretKey()).thenReturn(secretKey);

        LoginResponse actual = subject.refreshTokens(authorization);

        assertThat(actual.getRefreshToken()).isEqualTo(expected.getRefreshToken());
        assertThat(actual.getAccessToken()).isEqualTo(expected.getAccessToken());

        verify(converter).convert(decodedJWT);
        verify(decoder).decode(anyString());
        verify(storage).get(principal.getUserId());
        verify(userService).findById(id);
        verify(properties, times(2)).getSecretKey();
    }

    @Test
    void refreshTokensThrowRefreshTokenFailExceptionThatTokenIsForged() {
        String refreshToken = "~another refresh token~";

        when(converter.convert(decodedJWT)).thenReturn(principal);
        when(decoder.decode(anyString())).thenReturn(decodedJWT);
        when(storage.get(principal.getUserId())).thenReturn(refreshToken);

        assertThatThrownBy(() -> subject.refreshTokens(authorization))
                .isInstanceOf(RefreshTokenFailException.class)
                .hasMessage("Refresh token is forged!");

    }

    @Test
    void refreshTokensThrowRefreshTokenFailExceptionThatTokenIsNotValid() {
        when(converter.convert(decodedJWT)).thenReturn(principal);
        when(decoder.decode(anyString())).thenReturn(decodedJWT);
        when(storage.get(principal.getUserId())).thenReturn(null);

        assertThatThrownBy(() -> subject.refreshTokens(authorization))
                .isInstanceOf(RefreshTokenFailException.class)
                .hasMessage("Refresh token is not valid");

    }

    @Test
    void issueAccessToken() {
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
        UserPrincipal expected = UserPrincipal.builder().userId(id).build();

        when(userService.findById(id)).thenReturn(user);

        assertThat(subject.loadUserById(id).getUserId()).isEqualTo(expected.getUserId());

        verify(userService).findById(id);
    }
}