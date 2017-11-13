/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;


/**
 * Created by randi on 19/10/17.
 */

public class HabitTest {

    HabitList habits = new HabitList();
    Date startDate = new Date();
    Date eventDate = new Date();
    ArrayList<Day> array = new ArrayList<Day>();

    public HabitTest(){
        super();
    }


    @Test
    public void testGetTitle() throws DataFormatException {
        String title = "Test Title";
        try {
            Habit habit = new Habit(title, "Reason", startDate, array);
            String test_title = habit.getTitle();
            assertEquals(title, test_title);
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void testSetTitle() throws DataFormatException {
        String newTitle = "new Title";
        Habit habit = new Habit("old title", "Reason", startDate, array);
        habit.setTitle(newTitle);
        assertEquals(habit.getTitle(), newTitle);
    }

    @Test
    public void testGetReason() throws DataFormatException {
        String reason = "Reason";
        try {
            Habit habit = new Habit("Test Title", reason, startDate, array);
            String test_reason = habit.getReason();
            assertEquals(reason, test_reason);
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void testSetReason() throws DataFormatException {
        String newReason = "new Reason";
        Habit habit = new Habit("Test Title", "old reason", startDate, array);
        habit.setReason(newReason);
        assertEquals(habit.getReason(), newReason);
    }

    @Test
    public void testGetStartDate() throws DataFormatException {
        try {
            Habit habit = new Habit("Test Title", "Reason", startDate, array);
            Date test_startDate = habit.getStartDate();
            assertEquals(startDate, test_startDate);
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void testSetStartDate() throws DataFormatException {
        Date newStartDate = new Date();
        Habit habit = new Habit("Test Title", "Reason", startDate, array);
        habit.setStartDate(newStartDate);
        assertEquals(habit.getStartDate(), newStartDate);
    }

    @Test
    public void testGetFrequency() throws DataFormatException {
        try {
            array.add(Day.MONDAY);
            array.add(Day.WEDNESDAY);
            array.add(Day.FRIDAY);
            Habit habit = new Habit("Test Title", "Reason", startDate, array);
            ArrayList<Day> test_array = new ArrayList<Day>();
            test_array = habit.getFrequency();
            assertEquals(array, test_array);
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void testSetFrequency() throws DataFormatException {
        ArrayList<Day> newArray = new ArrayList<Day>();
        array.add(Day.MONDAY);
        array.add(Day.WEDNESDAY);
        array.add(Day.FRIDAY);

        newArray.add(Day.TUESDAY);
        newArray.add(Day.THURSDAY);

        Habit habit = new Habit("Test Title", "Reason", startDate, array);
        ArrayList<Day> test_array = new ArrayList<Day>();
        habit.setFrequency(newArray);
        assertEquals(habit.getFrequency(), newArray);
    }

    @Test
    public void testGetEvents() throws DataFormatException {
        try {
            Habit habit = new Habit("Test Title", "reason", startDate, array);
            HabitEvent habitEvent = new HabitEvent(habit, eventDate, "comment");
            HabitEventList habitEventList = new HabitEventList();

            habitEventList.add(habitEvent);
            habit.setEvents(habitEventList);

            HabitEventList returnedEventList = habit.getEvents();

            assertEquals(returnedEventList.getHabitEvent(0), habitEvent);
        }catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testSetEvents() throws DataFormatException {
        HabitEventList habitEventList = new HabitEventList();
        try {
            Habit habit = new Habit("Test Title", "reason", startDate, array);
            HabitEvent habitEvent = new HabitEvent(habit, eventDate, "comment");

            habitEventList.add(habitEvent);
            habit.setEvents(habitEventList);

            assertEquals(habit.getEvents(), habitEventList);
        }catch(Exception e){
            fail();
        }
    }
  
}
