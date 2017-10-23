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


/**
 * Created by randi on 19/10/17.
 */

public class HabitTest {

    HabitList habits = new HabitList();
    Date startDate = new Date();
    Date eventDate;
    ArrayList<Day> array = new ArrayList<Day>();

    public HabitTest(){
        super();
    }

    @Test
    public void testGetTitle() {
        String title = "Test Title";
        Habit habit = new Habit(title, "Reason", startDate, array);
        String test_title = habit.getTitle();
        assertEquals(title, test_title);
    }

    @Test
    public void testGetReason() {
        String reason = "Reason";
        Habit habit = new Habit("Test Title", reason, startDate, array);
        String test_reason = habit.getReason();
        assertEquals(reason, test_reason);
    }

    @Test
    public void testGetStartDate() {
        Habit habit = new Habit("Test Title", "Reason", startDate, array);
        Date test_startDate = habit.getStartDate();
        assertEquals(startDate, test_startDate);
    }

    @Test
    public void testGetFrequency() {
        array.add(Day.MONDAY);
        array.add(Day.WEDNESDAY);
        array.add(Day.FRIDAY);
        Habit habit = new Habit("Test Title", "Reason", startDate, array);
        ArrayList<Day> test_array = new ArrayList<Day>();
        test_array = habit.getFrequency();
        assertEquals(array, test_array);
    }

    @Test
    public void testGetEvents() {
        Habit habit = new Habit("Test Title", "reason", startDate, array);
        HabitEvent habitEvent = new HabitEvent(habit, eventDate, "comment");
        HabitEventList habitEventList = new HabitEventList();

        habitEventList.add(habitEvent);
        habit.setEvents(habitEventList);

        HabitEventList returnedEventList = habit.getEvents();

        assertEquals(returnedEventList.getHabitEvent(0), habitEvent);

    }

    @Test
    public void testSetEvents() {
        HabitEventList habitEventList = new HabitEventList();
        Habit habit = new Habit("Test Title", "reason", startDate, array);
        HabitEvent habitEvent = new HabitEvent(habit, eventDate, "comment");

        habitEventList.add(habitEvent);
        habit.setEvents(habitEventList);

        assertEquals(habit.getEvents(), habitEventList);

    }

}