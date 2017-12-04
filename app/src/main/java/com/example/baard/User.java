/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    private HashMap<String, Boolean> friends = new HashMap<String, Boolean>();
    private HashMap<String, String> receivedRequests = new HashMap<String, String>();

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

    public HashMap<String, Boolean> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, Boolean> friends) {
        this.friends = friends;
    }

    public HashMap<String, String> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(HashMap<String, String> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nUsername: " + username + "\n";
    }

}