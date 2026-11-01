package com.learnings.springsecurity.security;

import com.learnings.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //get our user from db
        com.learnings.springsecurity.entity.User userByUserEmail = userService.getUserByUserEmail(email);

        //this is spring user object, which is used for authentication and authorization
        //currently we are passing authorities as empty list as we are focusing on authentication only
        return org.springframework.security.core.userdetails.User.withUsername(userByUserEmail.getUserEmail())
                .password("{noop}" + userByUserEmail.getPassword()) //adding {noop} prefix as we are using plain text passwords which are not production recommendation
                .authorities(new ArrayList<>())
                .build();
    }
}
