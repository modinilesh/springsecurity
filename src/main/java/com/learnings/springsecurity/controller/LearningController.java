package com.learnings.springsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LearningController {

    @GetMapping("/learning/topic")
    public ResponseEntity<String> getLearningTopic() {
        return new ResponseEntity<>("Today we are implementing Inbound/Outbound audit system", HttpStatus.OK);
    }
}
