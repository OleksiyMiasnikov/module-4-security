package com.epam.esm.service;

import com.epam.esm.model.login.LoginRequest;
import com.epam.esm.model.login.LoginResponse;
import com.epam.esm.security.JwtIssuer;
import com.epam.esm.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final JwtIssuer issuer;

    private final AuthenticationManager authenticationManager;

    public LoginResponse attemptLogin(String name, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(name, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return LoginResponse.builder()
                .accessToken(issuer.issue(principal.getUserId(), principal.getName(), roles))
                .build();
    }

}
