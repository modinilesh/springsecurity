package com.learnings.springsecurity.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learnings.springsecurity.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.learnings.springsecurity.entity.User userByUserEmail;
        try {
            userByUserEmail = userService.getUserByUserEmail(email);
        } catch (IllegalArgumentException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }

        //this is spring user object, which is used for authentication and authorization
        //currently we are passing authorities as empty list as we are focusing on authentication only
        return org.springframework.security.core.userdetails.User.withUsername(userByUserEmail.getUserEmail())
                .password(userByUserEmail.getPassword()) //This is the encoded password, which is retrieved from the database
                .authorities(new ArrayList<>())
                .build();
    }
}
