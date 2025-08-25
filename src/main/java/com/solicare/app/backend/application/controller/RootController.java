package com.solicare.app.backend.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public String root() {
        return "Welcome! This is the default API page of Solicare.";
    }
}
