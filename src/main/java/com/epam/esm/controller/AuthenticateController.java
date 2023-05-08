package com.epam.esm.controller;

import com.epam.esm.model.DTO.login.LoginRequest;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.security.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request){
        LoginResponse loginResponse = service.attemptLogin(request.getName(), request.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", loginResponse.getAccessToken());
        headers.add("refresh_token", loginResponse.getRefreshToken());

        return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
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

    @GetMapping("/refresh")
    public LoginResponse refreshTokens() {
        return service.refreshTokens();
    }
}

