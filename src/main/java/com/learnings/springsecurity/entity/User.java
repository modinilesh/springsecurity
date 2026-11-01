package com.learnings.springsecurity.entity;

import com.learnings.springsecurity.config.SecurityConfig;

public class User {
    String userName;
    String userEmail;
    String password;

    //add more fields as per needs

    public User () {

    }


    public User(String userName, String userEmail, String password) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //Hash the password before setting to the entity to avoid password leak
        //once this password is hashed, then it can not be retrieved to plain password again
        this.password =  SecurityConfig.passwordEncoder().encode(password); //here encode method name is misleading as this uses hashing rather encoding
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
