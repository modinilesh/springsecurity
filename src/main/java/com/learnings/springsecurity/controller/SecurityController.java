package com.learnings.springsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/learnings/session")
    public ResponseEntity<String> getLearningSession() {
        return new ResponseEntity<>("Hi, today's session is Authentication and Authorization", HttpStatus.OK);
    }
}
