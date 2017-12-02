/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.searchbox.annotations.JestId;

/**
 * Created by biancaangotti on 2017-10-18.
 */

public class User {
    private String name;
    private String username;
    private HabitList habits = new HabitList();
//    private Map<String, Boolean> friends;
//    private ArrayList<String> receivedRequests = new ArrayList<>();
    private UserList friends = new UserList();
    private UserList receivedRequests = new UserList();

    @JestId
    private String id;

    public User(String name, String username, String id) {
        this.name = name;
        this.username = username;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

//    public Map<String, Boolean> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(Map<String, Boolean> friends) {
//        this.friends = friends;
//    }
//
//    public ArrayList<String> getReceivedRequests() {
//        return receivedRequests;
//    }
//
//    public void setReceivedRequests(ArrayList<String> receivedRequests) {
//        this.receivedRequests = receivedRequests;
//    }

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

    @Override
    public String toString() {
        return "Name: " + name + "\nUsername: " + username + "\n";
    }

}