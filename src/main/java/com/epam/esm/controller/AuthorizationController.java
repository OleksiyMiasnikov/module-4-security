package com.epam.esm.controller;

import com.epam.esm.model.login.LoginRequest;
import com.epam.esm.model.login.LoginResponse;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.service.AuthorizationService;
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
public class AuthorizationController {

    private final AuthorizationService service;

    @GetMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return service.attemptLogin(request.getName(), request.getPassword());
    }

    @GetMapping("/signup")
    public String signUp(){
        return "signup";
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

    @GetMapping("/admin")
    public String adminEndpoint(@AuthenticationPrincipal UserPrincipal principal) {
        return "This is secured admin endpoint!\n" +
                "You are authorized!\n" +
                "Name: " +
                principal.getName() +
                "\nRole: " +
                principal.getAuthorities();
    }


}

