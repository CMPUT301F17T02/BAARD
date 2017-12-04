/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Entities;

import android.support.annotation.NonNull;

import com.example.baard.Controllers.ElasticSearchController;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import io.searchbox.annotations.JestId;


/**
 * Defines a user for the BAARD Habit Tracker
 * @author bangotti, minsoung
 * @since 1.0
 * @version 3.0
 * @see HabitList
 */
public class User implements Comparable<User> {
    private String name;
    private String username;
    private HabitList habits = new HabitList();
    private HashMap<String, Boolean> friends = new HashMap<String, Boolean>();
    private HashMap<String, Boolean> receivedRequests = new HashMap<String, Boolean>();

  
    @JestId
    private String id;

    /**
     * Constructor
     * @param name User's name
     * @param username User's unique username
     * @param id User's unique Id for elastic search
     */
    public User(String name, String username, String id) {
        this.name = name;
        this.username = username;
        this.id = id;
    }

    /**
     * @return User id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username new String username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Habit list of all habits
     */
    public HabitList getHabits() {
        return habits;
    }

    /**
     * @param habits new Habit list
     */
    public void setHabits(HabitList habits) {
        this.habits = habits;
    }

    /**
     * @return Hash map of User's friends
     */
    public HashMap<String, Boolean> getFriends() {
        return friends;
    }

    /**
     * @param friends new Hash of friends
     */
    public void setFriends(HashMap<String, Boolean> friends) {
        this.friends = friends;
    }

    /**
     * @return HashMap of received Requests for friends
     */
    public HashMap<String, Boolean> getReceivedRequests() {
        return receivedRequests;
    }

    public HashMap<String, String> getAllUsers() {
        HashMap<String, String> userMap = new HashMap<String, String>();
        ElasticSearchController.GetAllUsersTask getAllUsersTask = new ElasticSearchController.GetAllUsersTask();
        getAllUsersTask.execute();
        UserList allUsers;

        try {
            allUsers = getAllUsersTask.get();
            for (User aUser : allUsers.getArrayList()) {
                userMap.put(aUser.getUsername(), aUser.getName());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return userMap;
    }
  
    /**
     * @param receivedRequests new Hash of received requests
     */
    public void setReceivedRequests(HashMap<String, Boolean> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    /**
     * @return String representation of user
     */
    @Override
    public String toString() {
        return "Name: " + name + "\nUsername: " + username + "\n";
    }

    /* Compares Users to each other. Calling Collection.sort will sort them in ascending
     * order for display anywhere
     * @param user
     * @return
     */
    @Override
    public int compareTo(@NonNull User user) {
        return this.getName().toLowerCase().compareTo(user.getName().toLowerCase());
    }
}