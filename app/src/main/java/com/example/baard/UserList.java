/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;

/**
 * Created by biancaangotti on 2017-10-20.
 */

public class UserList {


    private ArrayList<User> users = new ArrayList<User>();

    public UserList(){

    }

    public User getUser(int index){
        return users.get(index);
    }

    public boolean hasUser(User user){
        return users.contains(user);
    }

    public void add(User user) {
        users.add(user);
    }

    public void delete(User user) {
        users.remove(user);
    }

    public ArrayList<User> getArrayList() { return users; }

    public int size() {
        return users.size();
    }


}
