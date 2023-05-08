package com.epam.esm.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.esm.exception.NonAuthenticateUserException;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.model.entity.User;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.jwt.JwtProperties;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserService userService;
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
                    .accessToken(issueAccessToken(principal.getUserId(), principal.getName(), roles))
                    .refreshToken(issueRefreshToken(principal.getUserId()))
                    .build();
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            throw new NonAuthenticateUserException(exception.getMessage());
        }
    }

    /**
     * Authenticates the user by the given name and password,
     * gets the UserPrincipal of the authenticated user
     * and creates a json web token with the authenticated user.
     *
     * @return {@link LoginResponse} with created json web token
     */
    public LoginResponse refreshTokens() {
        log.info("Authenticating user.");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            principal = loadUserById(principal.getUserId());

            List<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return LoginResponse.builder()
                    .accessToken(issueAccessToken(principal.getUserId(), principal.getName(), roles))
                    .refreshToken(issueRefreshToken(principal.getUserId()))
                    .build();
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            throw new NonAuthenticateUserException(exception.getMessage());
        }
    }

    /**
     * Creates access json web token with given userId, name and lists of roles.
     *
     * @param userId users id
     * @param name name of user
     * @param roles list of roles of the given user
     * @return access json web token
     */
    public String issueAccessToken(Long userId, String name, List<String> roles){
        log.info("Issuing the new JWT.");
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(15, ChronoUnit.MINUTES)))
                .withClaim("name", name)
                .withClaim("authorities", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }

    /**
     * Creates refresh json web token with given userId.
     *
     * @param userId users id
     * @return refresh json web token
     */
    public String issueRefreshToken(Long userId){
        log.info("Issuing the new JWT.");
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.HOURS)))
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }


    /**
     * Gets user with a given id from database
     * and creates {@link UserPrincipal} with users details
     *
     * @param id users id
     * @return {@link UserPrincipal} with users details
     */
    public UserPrincipal loadUserById(Long id) {
        User user = userService.findById(id);
        log.info("Creating UserPrincipal of refreshed user.");
        return UserPrincipal.builder()
                .userId(user.getId())
                .name(user.getName())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .password(user.getPassword())
                .build();
    }
}
