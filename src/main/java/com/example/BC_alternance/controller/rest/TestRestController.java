package com.example.BC_alternance.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

    @GetMapping("/test")
    public String test() {
        return "Hello from BC Alternance!";
    }
}
