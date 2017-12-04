/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Entities;

import java.util.ArrayList;

/**
 * Defines a list of users
 * @author bangotti, rderbysh
 * @since 1.0
 */
public class UserList {

    private ArrayList<User> users = new ArrayList<>();

    /**
     * Constructor
     */
    public UserList(){}

    /**
     * @param index index in list
     * @return User at index
     */
    public User getUser(int index){
        return users.get(index);
    }

    /**
     * @param user User to search
     * @return true if found
     */
    public boolean hasUser(User user){
        return users.contains(user);
    }

    /**
     * @param user user to add
     */
    public void add(User user) {
        users.add(user);
    }

    /**
     * @param user user to delete
     */
    public void delete(User user) {
        users.remove(user);
    }

    /**
     * @return size of list
     */
    public int size() {return users.size();}

    /**
     * @return Array list representation of list
     */
    public ArrayList<User> getArrayList() { return users; }

}
