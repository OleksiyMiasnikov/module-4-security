package com.epam.esm.controller;

import com.epam.esm.model.DTO.login.LoginRequest;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthenticateController {

    private final AuthenticateService service;

    @GetMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        // todo
        // add refresh token
        return service.attemptLogin(request.getName(), request.getPassword());
    }

    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal UserPrincipal principal) {
        return "This is secured endpoint!\n" +
                "You are authorized!\n" +
                "Name: " +
                principal.getName() +
                "\nRole: " +
                principal.getAuthorities();
    }

    @GetMapping("/refresh_token")
    public String adminEndpoint(@AuthenticationPrincipal UserPrincipal principal) {
        //todo
        //1. verify refresh token
        //2. get user from token
        //3. generate new tokens for this user
        return "refresh token";
    }
}

