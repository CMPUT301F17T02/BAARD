/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;


/**
 * A class for testing the functionality of the HabitEvent class.
 * @author amckerna
 * @version 1.0
 */
public class HabitEventTests {

    ArrayList<Day> freq;
    Date date1;
    Habit habit;
    HabitEvent habitEvent;

    /**
     * This function is run at the beginning of each test. It sets up the Habit and HabitEvent required for each test.
     */
    @Before
    public void setUpTest(){
        freq = new ArrayList<Day>();
        date1 = new Date();
        // constructor 1
        try {
            habit = new Habit("test","reason",date1,freq);
            habitEvent = new HabitEvent(habit, date1, "testevent");
        }catch(Exception e){
            fail("No exception should be thrown.");
        }
    }

    /**
     * A test of all of the different constructors of HabitEvent.
     */
    @Test
    public void testConstructor(){
        // constructor 1
        try {
            habitEvent = new HabitEvent(habit, date1, "testevent");
            // test that the values have been set
            assertEquals(habitEvent.getComment(), "testevent");
            assertEquals(habitEvent.getEventDate(), date1);
            assertEquals(habitEvent.getHabit(), habit);
            // constructor 2
            Date date2 = new Date();
            HabitEvent habitEvent1 = new HabitEvent(habit, date2);
            // default comment should be empty string
            assertEquals(habitEvent1.getComment(), "");
            assertEquals(habitEvent1.getEventDate(), date2);
            assertEquals(habitEvent1.getHabit(), habit);
        }catch(Exception e){
            fail("No exception should be thrown");
        }
    }

    /**
     * Test the getComment functionality of the HabitEvent class
     */
    @Test
    public void testGetComment(){
        try {
            HabitEvent habitEvent = new HabitEvent(habit, date1, "testevent");
            assertEquals(habitEvent.getComment(), "testevent");
        }catch(Exception e){
            fail("No exception should be thrown");
        }
    }

    /**
     * Test the setComment functionality of the HabitEvent class.
     */
    @Test
    public void testSetComment(){
        try {
            habitEvent.setComment("set test");
            assertEquals(habitEvent.getComment(), "set test");
        }catch(Exception e){
            fail("No exception should be thrown");
        }
    }

    /**
     * Test the getEventDate functionality of the HabitEvent class
     */
    @Test
    public void testGetDate(){
        assertEquals(habitEvent.getEventDate(), date1);
    }
    @Test
    /**
     * Test the setEventDate functionality of the HabitEvent class
     */
    public void testSetDate(){
        Date date2 = new Date();

        try {
            habitEvent.setEventDate(date2);
            //assertNotEquals(habitEvent.getEventDate(), date1);
            assertEquals(habitEvent.getEventDate(), date2);
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Test the getHabit functionality of the HabitEvent class.
     */
    @Test
    public void testGetHabit(){
        assertEquals(habitEvent.getHabit(), habit);
    }

    /**
     * Test the setHabit functionality of the HabitEvent class.
     */
    @Test
    public void testSetHabit(){
        try {
            ArrayList<Day> freq2 = new ArrayList<Day>();
            Date date2 = new Date();
            Habit habit2 = new Habit("test2", "better reason", date2, freq2);
            habitEvent.setHabit(habit2);
            assertNotEquals(habitEvent.getHabit(), habit);
            assertEquals(habitEvent.getHabit(), habit2);
        }catch(Exception e){
            fail();
        }
    }

}