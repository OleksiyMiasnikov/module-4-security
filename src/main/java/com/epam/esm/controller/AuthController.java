package com.epam.esm.controller;

import com.epam.esm.model.LoginRequest;
import com.epam.esm.model.LoginResponse;
import com.epam.esm.security.JwtIssuer;
import lombok.RequiredArgsConstructor;
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
    @GetMapping()
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return LoginResponse.builder()
                .accessToken(issuer.issue(1, request.getName(), List.of("USER")))
                .build();

    }

}

