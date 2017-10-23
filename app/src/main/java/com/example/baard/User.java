/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;

/**
 * Created by biancaangotti on 2017-10-18.
 */

public class User {
    private String name;
    private String username;
    private HabitList habits = new HabitList();
    private UserList friends = new UserList();
    private UserList receivedRequests = new UserList();
    // WOW factor TODO --> profile pictures

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HabitList getHabits() {
        return habits;
    }

    public void setHabits(HabitList habits) {
        this.habits = habits;
    }

    public UserList getFriends() {
        return friends;
    }

    public void setFriends(UserList friends) {
        this.friends = friends;
    }

    public UserList getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(UserList receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

}