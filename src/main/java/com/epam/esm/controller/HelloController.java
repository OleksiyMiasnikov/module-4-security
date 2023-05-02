package com.epam.esm.controller;

import com.epam.esm.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class HelloController {

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

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
