package com.epam.esm.controller;

import com.epam.esm.model.login.LoginRequest;
import com.epam.esm.model.login.LoginResponse;
import com.epam.esm.security.JwtIssuer;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.UserPrincipalAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    private final JwtIssuer issuer;

    private final AuthenticationManager authenticationManager;

    @GetMapping()
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
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

