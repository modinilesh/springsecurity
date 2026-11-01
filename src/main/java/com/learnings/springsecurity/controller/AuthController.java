package com.learnings.springsecurity.controller;

import com.learnings.springsecurity.entity.User;
import com.learnings.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "home";
    }

    @GetMapping("/signin")
    public String signIn() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "register";
    }

    @PostMapping("/signup")
    public String register(User user) {
        userService.createUser(user);
        return "redirect:/signin?registered";
    }
}
