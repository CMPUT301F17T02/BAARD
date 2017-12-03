/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Implements testing for the DailyHabits Frag,emt
 * @see android.test.ActivityInstrumentationTestCase2
 * @see DailyHabitsFragment
 * @see MainActivity
 * @author anarten
 */
public class DailyHabitsTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);

    /**
     * Constructor for the AllHabitsTest Class
     */
    public DailyHabitsTest() {
        super(com.example.baard.LoginActivity.class);
    }

    /**
     * Setup function for InstrumentationTest Cases
     */
    @Override
    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
        // log out if we are logged in for each test
        if (!(solo.searchButton("Register", true))) {
            solo.clickOnImage(0);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        }
        Log.d("SETUP", "setUp()");

        // sign the testing user in
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        // go to main activity
        solo.waitForActivity(MainActivity.class, 2000);

        solo.assertCurrentActivity("wrong acitivty", MainActivity.class);
        solo.clickOnImage(0);
        solo.clickOnText("Daily Habits");
        solo.waitForFragmentById(R.layout.fragment_daily_habits, 2000);
    }

    @Test
    public void testDailyHabitAdd() {

        Date today = new Date();
        String todayDofW = sdf.format(today);
        if (todayDofW.equals("Monday")) {

        }

        solo.clickOnImage(0);
        solo.clickOnText("Create New Habit");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit, 2000);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_create_new_habit);

        // fill out the details for the habit
        EditText title = (EditText) solo.getView(R.id.title);
        EditText reason = (EditText) solo.getView(R.id.reason);

        solo.enterText(title, "Swimming");
        solo.enterText(reason, "IExercise");
        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        solo.clickOnText("Mon");
        solo.clickOnButton("Create");

        // ensure the page moved to view for success & detail there
        solo.waitForActivity(ViewHabitActivity.class, 2000);

        // go to main page and check it is in list
        solo.clickOnImage(0);
        solo.clickOnImage(0);
        solo.clickOnText("All Habits");

        solo.waitForFragmentById(R.layout.fragment_all_habits, 2000);
        assertTrue(solo.searchText("Swimming", 1, true, true));
    }



}
