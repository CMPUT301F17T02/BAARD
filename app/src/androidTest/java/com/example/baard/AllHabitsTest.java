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

/**
 * Created by Adam on 12/2/2017.
 */

public class AllHabitsTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    /**
     * Constructor for the AllHabitsTest Class
     */
    public AllHabitsTest() {
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
        solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
    }

    @Test
    public void testAllHabitsExist() {
        assertTrue(solo.searchText("Jogging", 1, true, true));
    }

    @Test
    public void testViewFromAll() {
        solo.clickOnText("Jogging");
        solo.clickOnButton("View");
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);
        assertTrue(solo.searchText("Jogging", 1, true, true));

        solo.clickOnImage(0);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
    }

    @Test
    public void testEditFromAll() {
        solo.clickOnText("Jogging");
        solo.clickOnButton("Edit");
        solo.assertCurrentActivity("wrong activity", EditHabitActivity.class);
        assertTrue(solo.searchText("Jogging", 1, true, true));

        solo.clickOnImage(0);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);
    }

    @Test
    public void testDeleteFromAll() {
        //TODO
    }

    @Override
    public void tearDown() {
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

        solo.finishOpenedActivities();
    }

}
