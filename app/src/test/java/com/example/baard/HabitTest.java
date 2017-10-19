/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by randi on 19/10/17.
 */

public class HabitTest extends ActivityInstrumentationTestCase2 {
    public HabitTest(){
        super(Habit.class);
    }

    public void testGetTitle() {
        ArrayList<Habit> habits = new ArrayList<Habit>();
        Date startDate = new Date();
        Habit habit = new Habit("Test Title", "Reason", startDate);
    }

    public void testGetReason() {

    }

    public void testGetStartDate() {

    }

    public void testGetFrequency() {

    }

    public void testGetEvent() {

    }

    public void testAddEvent() {

    }

    public void testRemoveEvent() {

    }
}
