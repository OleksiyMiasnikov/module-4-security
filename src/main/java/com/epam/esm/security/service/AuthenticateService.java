package com.epam.esm.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.esm.exception.NonAuthenticateUserException;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Authenticates the user by the given name and password,
 * gets the UserPrincipal of the authenticated user
 * and creates a json web token with the authenticated user.
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties properties;

    /**
     * Authenticates the user by the given name and password,
     * gets the UserPrincipal of the authenticated user
     * and creates a json web token with the authenticated user.
     *
     * @param name a given name of user
     * @param password a given password
     * @return {@link LoginResponse} with created json web token
     */
    public LoginResponse attemptLogin(String name, String password) {
        log.info("Authenticating user.");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(name, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            List<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return LoginResponse.builder()
                    .accessToken(issue(principal.getUserId(), principal.getName(), roles))
                    .build();
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            throw new NonAuthenticateUserException(exception.getMessage());
        }


    }

    /**
     * Creates json web token with given userId, name and lists of roles.
     *
     * @param userId users id
     * @param name name of user
     * @param roles list of roles of the given user
     * @return json web token
     */
    public String issue(Long userId, String name, List<String> roles){
        log.info("Issuing the new JWT.");
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(15, ChronoUnit.MINUTES)))
                .withClaim("name", name)
                .withClaim("authorities", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }
}
