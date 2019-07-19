package com.example.vacationmalangver1.util;

import android.app.Application;

import com.example.vacationmalangver1.Model.User;


public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
