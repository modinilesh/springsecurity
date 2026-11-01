package com.learnings.springsecurity.entity;

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
        this.password = password;
    }
}
