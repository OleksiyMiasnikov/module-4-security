package com.epam.esm.controller;

import com.epam.esm.model.login.LoginRequest;
import com.epam.esm.model.login.LoginResponse;
import com.epam.esm.security.JwtIssuer;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.UserPrincipalAuthenticationToken;
import com.epam.esm.service.AuthorizationService;
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

    private final AuthorizationService service;

    @GetMapping()
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return service.attemptLogin(request.getName(), request.getPassword());
    }

}

