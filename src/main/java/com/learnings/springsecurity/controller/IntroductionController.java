package com.learnings.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntroductionController {

	@GetMapping("/")
	public String home() {
		return "Hi! welcome to the sessions to learn request workflow";
	}
}
