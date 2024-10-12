package com.example.myapplication;

public class Users {
    String email,password,username;
    String userid;
    public Users(){}
    public Users(String userId, String userName, String email, String password) {
        this.userid = userId;
        this.username = userName;
        this.email = email;
        this.password = password;

    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
