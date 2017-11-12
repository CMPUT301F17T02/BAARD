/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.robotium.solo.Solo;

import java.util.zip.DataFormatException;

/**
 * Created by randi on 12/11/17.
 */

public class EditHabitTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public EditHabitTest() throws DataFormatException {
        super(MainActivity.class);
    }
//
//    Date startDate = new Date();
//    ArrayList<Day> frequency = new ArrayList<>();
//    Habit habit = new Habit("title", "Reason", startDate, frequency);

    @Override
    protected void setUp() throws Exception {
        // get instrumentation get all info from device writing on
        // get activity gets link to lonely twitter
        Activity activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        Log.d("SETUP","setUp()");
    }

    public void testEditHabit() throws Exception {
        Activity activity = getActivity();
        solo.waitForActivity(MainActivity.class, 2000);

        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("All Habits");

        // Now need to click on a habit and test the editing of the habit.

    }
}
