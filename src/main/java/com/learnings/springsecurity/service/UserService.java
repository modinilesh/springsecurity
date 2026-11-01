package com.learnings.springsecurity.service;

import com.learnings.springsecurity.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final List<User> DB_USERS = new ArrayList<>();

    static {
        User user1 = new User("Nilesh", "nileshdev@gmail.com", "nildev123"); //is storing password directly is good habit ?
        User user2 = new User("Nilesh2", "nileshdev2@gmail.com", "nildev2123"); //is storing password directly is good habit ?
        DB_USERS.add(user1);
        DB_USERS.add(user2);
    }

    //This can be your actual service call which uses repository for db fetch
    public User getUserByUserEmail(String email) {
        //validateEmail(email);

        //get user
        for (User user : DB_USERS) {
            if (user.getUserEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        throw new IllegalArgumentException("User Not Found with userEmail: " + email);
    }



}
