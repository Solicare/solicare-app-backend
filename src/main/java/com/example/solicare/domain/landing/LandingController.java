package com.example.solicare.domain.landing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingController {

    @GetMapping("/landing")
    public String landing() {
        return "안녕하세요";
    }
}
