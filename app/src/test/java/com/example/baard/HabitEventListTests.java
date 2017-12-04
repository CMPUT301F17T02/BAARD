/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

/**
 * Created by chrygore on 22/10/17.
 */



import com.example.baard.Entities.Day;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitEvent;
import com.example.baard.Entities.HabitEventList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

/**
 * Class for testing all the HabitEventList functionalities.
 * @author amckerna
 * @version 1.0
 */
public class HabitEventListTests {

    ArrayList<Day> freq;
    Date date1;
    Habit habit;
    HabitEvent habitEvent;
    HabitEventList habitEventList;

    /**
     * Run setup before each test.
     */
    @Before
    public void setUpTest() throws DataFormatException {
        freq = new ArrayList<Day>();
        date1 = new Date();
        // constructor 1
        try {
            habit = new Habit("test","reason",date1,freq);
            habitEvent = new HabitEvent(habit, date1, "testevent");
            habitEventList = new HabitEventList();
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Add a HabitEvent to the HabitList.
     */
    @Test
    public void testAdd(){
        habitEventList.add(habitEvent);
        assertTrue(habitEventList.hasHabitEvent(habitEvent));
    }

    /**
     * Delete a HabitEvent from the HabitList.
     */
    @Test
    public void testDelete(){
        habitEventList.add(habitEvent);
        habitEventList.delete(habitEvent);
        assertFalse(habitEventList.hasHabitEvent(habitEvent));
    }

    /**
     * Check if a given HabitEvent is within the HabitList.
     */
    @Test
    public void testHasHabitEvent(){
        habitEventList.add(habitEvent);
        Date date2 = new Date();
        try {
            HabitEvent habitEvent2 = new HabitEvent(habit, date2, "second");
            assertTrue(habitEventList.hasHabitEvent(habitEvent));
            assertFalse(habitEventList.hasHabitEvent(habitEvent2));
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Test returning a HabitEvent within the HabitList.
     */
    @Test
    public void testGetHabitEvent(){
        habitEventList.add(habitEvent);
        assertEquals(habitEventList.getHabitEvent(0), habitEvent);
    }
}