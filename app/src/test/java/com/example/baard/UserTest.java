/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;


import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;


/**
 * Created by mchoi on 2017-10-22.
 */

public class UserTest extends TestCase {

    private User user;

    public void setUp() {
        user = new User("Daniel", "daniel.choi123", "id");
    }

    public void testGetName() {
        assertEquals("Daniel", user.getName());
    }

    public void testSetName() {
        user.setName("Andrew");
        assertEquals("Andrew", user.getName());
    }

    public void testGetUsername() {
        assertEquals("daniel.choi123", user.getUsername());
    }

    public void testSetUsername() {
        user.setUsername("qwerty93");
        assertEquals("qwerty93", user.getUsername());
    }

    public void testGetHabits() throws DataFormatException {
        HabitList habitList = new HabitList();
        ArrayList<Day> frequency = new ArrayList<Day>();
        frequency.add(Day.MONDAY);
        try {
            Habit habit = new Habit("Jog", "To be healthy", new Date(), frequency);
            habitList.add(habit);
            user.setHabits(habitList);
            assertEquals(habitList, user.getHabits());
        }catch(Exception e){
            fail();
        }
    }

    public void testSetHabits() {
        try {
            HabitList habitList = new HabitList();
            ArrayList<Day> frequency = new ArrayList<Day>();
            frequency.add(Day.MONDAY);
            Habit habit = new Habit("Jog", "To be healthy", new Date(), frequency);
            habitList.add(habit);
            user.setHabits(habitList);
            assertEquals(habitList, user.getHabits());
        }catch(Exception e){
            fail();
        }
    }

    public void testGetFriends() {
        UserList userList = new UserList();
        userList.add(new User("John", "johnSmith232", "id"));
        user.setFriends(userList);
        assertEquals(userList, user.getFriends());
    }

    public void testSetFriends() {
        UserList userList = new UserList();
        userList.add(new User("John", "johnSmith232", "id"));
        user.setFriends(userList);
        assertEquals(userList, user.getFriends());
    }

    public void testGetReceivedRequests() {
        UserList userList = new UserList();
        userList.add(new User("John", "johnSmith232", "id"));
        user.setReceivedRequests(userList);
        assertEquals(userList, user.getReceivedRequests());
    }

    public void testSetReceivedRequests() {
        UserList userList = new UserList();
        userList.add(new User("John", "johnSmith232", "id"));
        user.setReceivedRequests(userList);
        assertEquals(userList, user.getReceivedRequests());
    }

}