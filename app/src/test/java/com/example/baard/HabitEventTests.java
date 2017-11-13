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

    //do we need this?? was in the lab test cases
    //public HabitEventTests{
    //super();
    //}

    ArrayList<Day> freq;
    Date date1;
    Habit habit;
    HabitEvent habitEvent;

    //this function is run at the start of every test
    @Before
    public void setUpTest() throws DataFormatException {
        freq = new ArrayList<Day>();
        date1 = new Date();
        habit = new Habit("test","reason",date1,freq);
        // constructor 1
        habitEvent = new HabitEvent(habit, date1, "testevent");
    }
    @Test
    public void testConstructor(){
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        //Date date1 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        habitEvent = new HabitEvent(habit, date1, "testevent");
        // test that the values have been set
        assertEquals(habitEvent.getComment(), "testevent");
        assertEquals(habitEvent.getEventDate(), date1);
        assertEquals(habitEvent.getHabit(), habit);
        // constructor 2
        Date date2 = new Date();
        HabitEvent habitEvent1 =new HabitEvent(habit, date2);
        // default comment should be empty string
        assertEquals(habitEvent1.getComment(), "");
        assertEquals(habitEvent1.getEventDate(), date2);
        assertEquals(habitEvent1.getHabit(), habit);
    }
    @Test
    public void testGetComment(){
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        //Date date1 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        HabitEvent habitEvent = new HabitEvent(habit, date1, "testevent");
        assertEquals(habitEvent.getComment(), "testevent");
    }
    @Test
    public void testSetComment(){
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        //Date date1 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        //HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        habitEvent.setComment("set test");
        assertEquals(habitEvent.getComment(), "set test");
    }
    @Test
    public void testGetDate(){
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        //Date date1 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        //HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        assertEquals(habitEvent.getEventDate(), date1);
    }
    @Test
    public void testSetDate(){
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        //Date date1 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        //HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        Date date2 = new Date();
        habitEvent.setEventDate(date2);
        //assertNotEquals(habitEvent.getEventDate(), date1);
        assertEquals(habitEvent.getEventDate(), date2);
    }
    @Test
    public void testGetHabit(){
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        //Date date1 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        // constructor 1
        //HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        assertEquals(habitEvent.getHabit(), habit);
    }
    @Test
    public void testSetHabit() throws DataFormatException {
        //ArrayList<Integer> freq = new ArrayList<Integer>();
        ArrayList<Day> freq2 = new ArrayList<Day>();
        //Date date1 = new Date();
        Date date2 = new Date();
        //Habit habit = new Habit("test","reason",date1,freq);
        Habit habit2 = new Habit("test2","better reason",date2,freq2);
        // constructor 1
        //HabitEvent habitEvent = new HabitEvent(habit, "testevent", date1);
        habitEvent.setHabit(habit2);
        assertNotEquals(habitEvent.getHabit(), habit);
        assertEquals(habitEvent.getHabit(), habit2);
    }

}