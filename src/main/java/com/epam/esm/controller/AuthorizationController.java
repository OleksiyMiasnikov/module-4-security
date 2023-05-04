package com.epam.esm.controller;

import com.epam.esm.model.DTO.login.LoginRequest;
import com.epam.esm.model.DTO.login.LoginResponse;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthorizationController {

    private final AuthenticateService service;

    @GetMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return service.attemptLogin(request.getName(), request.getPassword());
    }

    @GetMapping("/signup")
    public String signUp(){
        return "signup";
    }

    @GetMapping("/secured")
    @PreAuthorize("hasAnyRole('ADMIN','USER', 'GUEST')")
    public String secured(@AuthenticationPrincipal UserPrincipal principal) {
        return "This is secured endpoint!\n" +
                "You are authorized!\n" +
                "Name: " +
                principal.getName() +
                "\nRole: " +
                principal.getAuthorities();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint(@AuthenticationPrincipal UserPrincipal principal) {
        return "This is secured admin endpoint!\n" +
                "You are authorized!\n" +
                "Name: " +
                principal.getName() +
                "\nRole: " +
                principal.getAuthorities();
    }
}

