package com.epam.esm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/secured")
    public String secured() {
        return "This is secured endpoint!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
