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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthenticateController {

    private final AuthenticateService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request){
        LoginResponse loginResponse = service.attemptLogin(request.getName(), request.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccessControlExposeHeaders(List.of("access_token", "refresh_token"));
        headers.add("access_token", loginResponse.getAccessToken());
        headers.add("refresh_token", loginResponse.getRefreshToken());

        return new ResponseEntity<>(headers, HttpStatus.OK);
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
    public ResponseEntity<LoginResponse> refreshTokens(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        LoginResponse loginResponse = service.refreshTokens(authorization);
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", loginResponse.getAccessToken());
        headers.add("refresh_token", loginResponse.getRefreshToken());

        return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
    }
}

