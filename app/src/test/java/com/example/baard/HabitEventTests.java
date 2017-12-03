/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;


import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;


/**
 * A class for testing the functionality of the HabitEvent class.
 * @author amckerna
 * @version 1.0
 */
public class HabitEventTests {

    ArrayList<Day> freq;
    //Date date1 = new Date(2017/11/25);
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    Date date1;
    Habit habit;
    HabitEvent habitEvent;
    private String eventDate;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar calendar = Calendar.getInstance();

    /**
     * This function is run at the beginning of each test. It sets up the Habit and HabitEvent required for each test.
     */
    private Date createDate(String yyyymmdd) throws ParseException{
        return format.parse(yyyymmdd);
    }
    @Before
    public void setUpTest() throws DataFormatException, ParseException {
        date1 = format.parse("2017/11/25");
        freq = new ArrayList<Day>();
//        date1 = new Date();
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
            assertEquals(habitEvent.getEventDate().toString(), date1.toString());
            assertEquals(habitEvent.getHabit(), habit);
            // constructor 2
            Date date2 = createDate("2017/12/01");
            HabitEvent habitEvent1 = new HabitEvent(habit, date2);
            // default comment should be empty string
            assertEquals(habitEvent1.getComment(), "");
            assertEquals(habitEvent1.getEventDate().toString(), date2.toString());
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
    public void testGetDate() throws ParseException {

        //calendar.set(Calendar.YEAR, 2017);
        //calendar.set(Calendar.MONTH, 11);
        //calendar.set(Calendar.DAY_OF_MONTH, 25);
        //calendar.set(Calendar.HOUR, 0);
        //calendar.set(Calendar.MINUTE, 0);
        //calendar.set(Calendar.SECOND, 0);
        assertTrue(habitEvent.getEventDate().equals(createDate("2017/11/25")));
    }
    @Test
    /**
     * Test the setEventDate functionality of the HabitEvent class
     */
    public void testSetDate() throws ParseException{
        Date wrongDate = createDate("2017/12/01");
        Date rightDate = createDate("2017/11/30");
        try {
            habitEvent.setEventDate(wrongDate);
            assertFalse(habitEvent.getEventDate().equals(rightDate) );
            //assertNotEquals(habitEvent.getEventDate(), date1);
            habitEvent.setEventDate(rightDate);
            assertTrue(habitEvent.getEventDate().equals(rightDate));
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