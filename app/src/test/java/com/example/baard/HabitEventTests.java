/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by chrygore on 20/10/17.
 */

public class HabitEventTests {
    //do we need this??
    //public HabitEventTests{
        //super();
    //}

    public void testConstructor(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        Date date1 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        // test that the values have been set
        assertEquals(habitEvent.getComment(), "testevent");
        assertEquals(habitEvent.getEventDate(), date1);
        assertEquals(habitEvent.getHabit(), habit);
        // constructor 2
        Date date2 = new Date();
        HabitEvent habitEvent1 =new HabitEvent(habit, date2);
        // default comment should be empty string
        assertEquals(habitEvent.getComment(), "");
        assertEquals(habitEvent.getEventDate(), date2);
        assertEquals(habitEvent.getHabit(), habit);
    }

    public void testGetComment(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        Date date1 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        assertEquals(habitEvent.getComment(), "testevent");
    }

    public void testSetComment(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        Date date1 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        habitEvent.setComment("set test");
        assertEquals(habitEvent.getComment(), "set test");
    }

    public void testGetDate(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        Date date1 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        assertEquals(habitEvent.getEventDate(), date1);
    }

    public void testSetDate(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        Date date1 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        Date date2 = new Date();
        habitEvent.setEventDate(date2);
        assertNotEquals(habitEvent.getEventDate(), date1);
        assertEquals(habitEvent.getEventDate(), date2);
    }

    public void testGetHabit(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        Date date1 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        assertEquals(habitEvent.getHabit(), habit);
    }

    public void testSetHabit(){
        ArrayList<Integer> freq = new ArrayList<Integer>();
        ArrayList<Integer> freq2 = new ArrayList<Integer>();
        Date date1 = new Date();
        Date date2 = new Date();
        Habit habit = new Habit("test","reason",date1,freq);
        Habit habit2 = new Habit("test2","better reason",date2,freq2);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        habitEvent.setHabit(habit2);
        assertNotEquals(habitEvent.getHabit(), habit);
        assertEquals(habitEvent.getHabit(), habit2);
    }

}
