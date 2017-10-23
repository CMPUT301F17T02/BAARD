/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import junit.framework.TestCase;

/**
 * Created by mchoi on 2017-10-22.
 */

public class UserTest extends TestCase {

    private User user;

    @Override
    public void setUp() {
        user = new User("Daniel", "daniel.choi@gmail.com", "asdf1234", "daniel.choi123");
    }

    public void testGetName() {
        assertEquals(user.getName(), "Daniel");
    }
    public void testSetName() {
        user.setName("Andrew");
        assertEquals(user.getName(), "Andrew");
    }
    public void testGetEmail() {
        assertEquals(user.getEmail(), "daniel.choi@gmail.com");
    }
    public void testSetEmail() {
        user.setEmail("john.smith@gmail.com");
        assertEquals(user.getEmail(), "john.smith@gmail.com");
    }
    public void testGetPassword() {
        assertEquals(user.getPassword(), "asdf1234");
    }
    public void testSetPassword() {
        user.setPassword("1234asdf");
        assertEquals(user.getPassword(), "1234asdf");
    }
    public void testGetUsername() {
        assertEquals(user.getUsername(), "daniel.choi123");
    }
    public void testSetUsername() {
        user.setUsername("qwerty93");
        assertEquals(user.getUsername(), "qwerty93");
    }
    public void testGetHabits() {
    }
    public void testSetHabits() {

    }
    public void testGetFriends() {

    }
    public void testSetFriends() {

    }
    public void testGetReceivedRequests() {

    }
    public void testSetReceivedRequests() {

    }
}
