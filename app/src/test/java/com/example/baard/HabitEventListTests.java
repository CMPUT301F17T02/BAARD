/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

/**
 * Created by chrygore on 22/10/17.
 */



import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class HabitEventListTests {

    //do we need test constructor?

    ArrayList<Day> freq;
    Date date1;
    Habit habit;
    HabitEvent habitEvent;
    HabitEventList habitEventList;

    //run Before every test
    @Before
    public void setUpTest(){
        freq = new ArrayList<Day>();
        date1 = new Date();
        habit = new Habit("test","reason",date1,freq);
        // constructor 1
        try {
            habitEvent = new HabitEvent(habit, date1, "testevent");
            habitEventList = new HabitEventList();
        }catch(Exception e){
            fail();
        }
    }
    @Test
    public void testAdd(){
        habitEventList.add(habitEvent);
        assertTrue(habitEventList.hasHabitEvent(habitEvent));
    }
    @Test
    public void testDelete(){
        habitEventList.add(habitEvent);
        habitEventList.delete(habitEvent);
        assertFalse(habitEventList.hasHabitEvent(habitEvent));
    }
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
    @Test
    public void testGetHabitEvent(){
        habitEventList.add(habitEvent);
        assertEquals(habitEventList.getHabitEvent(0), habitEvent);
    }
}