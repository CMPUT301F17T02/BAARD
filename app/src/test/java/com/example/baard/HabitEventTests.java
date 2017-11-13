/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

/**
 * Created by chrygore on 20/10/17.
 */

public class HabitEventTests {

    ArrayList<Day> freq;
    Date date1;
    Habit habit;
    HabitEvent habitEvent;

    //this function is run at the start of every test
    @Before
    public void setUpTest() throws DataFormatException {
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
            assertEquals(habitEvent.getComment(), "");
            assertEquals(habitEvent.getEventDate(), date2);
            assertEquals(habitEvent.getHabit(), habit);
        }catch(Exception e){
            fail("No exception should be thrown");
        }
    }
    @Test
    public void testGetComment(){
        try {
            HabitEvent habitEvent = new HabitEvent(habit, date1, "testevent");
            assertEquals(habitEvent.getComment(), "testevent");
        }catch(Exception e){
            fail("No exception should be thrown");
        }
    }
    @Test
    public void testSetComment(){
        try {
            habitEvent.setComment("set test");
            assertEquals(habitEvent.getComment(), "set test");
        }catch(Exception e){
            fail("No exception should be thrown");
        }
    }
    @Test
    public void testGetDate(){
        assertEquals(habitEvent.getEventDate(), date1);
    }
    @Test
    public void testSetDate(){
        Date date2 = new Date();
        habitEvent.setEventDate(date2);
        //assertNotEquals(habitEvent.getEventDate(), date1);
        assertEquals(habitEvent.getEventDate(), date2);

    }
    @Test
    public void testGetHabit(){
        assertEquals(habitEvent.getHabit(), habit);
    }
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