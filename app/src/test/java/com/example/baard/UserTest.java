/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;


import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.DataFormatException;


/**
 * Created by mchoi on 2017-10-22.
 */

public class UserTest extends TestCase {

    private User user;
    HashMap<String, Boolean> userList = new HashMap<>();
    HashMap<String, String> userMap = new HashMap<>();

    public void setUp() {
        user = new User("Daniel", "daniel.choi123", "1");
        userList.put("username1", Boolean.TRUE);
        userList.put("username2", Boolean.FALSE);
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

    public void testGetHabits() {
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
        HabitList habitList = new HabitList();
        ArrayList<Day> frequency = new ArrayList<Day>();
        frequency.add(Day.MONDAY);
        try {
            Habit habit = new Habit("Jog", "To be healthy", new Date(), frequency);
            habitList.add(habit);
            user.setHabits(habitList);
            assertEquals(habitList, user.getHabits());
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetFriends() {
//        UserList userList = new UserList();
//        userList.add(new User("John", "johnSmith232", "2"));
        user.setFriends(userList);
        assertEquals("username1", user.getFriends());
    }

    public void testSetFriends() throws DataFormatException {
//        UserList userList = new UserList();
//        userList.add(new User("John", "johnSmith232", "2"));
        user.setFriends(userList);
        assertEquals(userList, user.getFriends());
    }

    public void testGetReceivedRequests() {
//        UserList userList = new UserList();
//        userList.add(new User("John", "johnSmith232", "2"));
        user.setReceivedRequests(userMap);
        assertEquals(userList, user.getReceivedRequests());
    }

    public void testSetReceivedRequests() {
//        UserList userList = new UserList();
//        userList.add(new User("John", "johnSmith232", "2"));
        user.setReceivedRequests(userMap);
        assertEquals(userList, user.getReceivedRequests());
    }

    public void testGetId() {
        assertEquals("1", user.getId());
    }

    public void testSetId() {
        user.setId("4");
        assertEquals("4", user.getId());
    }

    public void testToString() {
        assertEquals("Name: Daniel\nUsername: daniel.choi123\n", user.toString());
    }

}