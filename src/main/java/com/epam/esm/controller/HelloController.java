package com.epam.esm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {
    @GetMapping("/world")
    public String helloWorld() {
        return "Hello world!";
    }

    @GetMapping("")
    public String hello() {
        return "Hello!";
    }
}
