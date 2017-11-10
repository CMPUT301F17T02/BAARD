/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by randi on 23/10/17.
 */

public class HabitListTest {

    HabitList habits = new HabitList();
    Date startDate = new Date();
    Date eventDate;
    ArrayList<Day> array = new ArrayList<Day>();

    public HabitListTest(){
        super();
    }

    @Test
    public void testGetHabit() {
        Habit habit = new Habit("title", "Reason", startDate, array);
        habits.add(habit);
        Habit returnedHabit = habits.getHabit(0);

        assertEquals(returnedHabit.getTitle(), habit.getTitle());
        assertEquals(returnedHabit.getReason(), habit.getReason());
        assertEquals(returnedHabit.getFrequency(), habit.getFrequency());
    }

    @Test
    public void testHasHabit() {
        Habit habit = new Habit("Test Title", "reason", startDate, array);

        assertFalse(habits.hasHabit(habit));
        habits.add(habit);
        assertTrue(habits.hasHabit(habit));
    }

    @Test
    public void testAdd() {
        Habit habit = new Habit("Test Title", "reason", startDate, array);

        habits.add(habit);
        assertTrue(habits.hasHabit(habit));
    }

    @Test
    public void testDelete() {
        Habit habit = new Habit("Test Title", "reason", startDate, array);
        habits.add(habit);
        habits.delete(habit);
        assertFalse(habits.hasHabit(habit));
    }
}