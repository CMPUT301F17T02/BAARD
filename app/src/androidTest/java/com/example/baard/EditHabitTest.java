/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

/**
 * Created by randi on 12/11/17.
 */

public class EditHabitTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public EditHabitTest() throws DataFormatException {
        super(LoginActivity.class);
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

        // if already logged in, log out
        if (!(solo.searchButton("Register", true))) {
            solo.clickOnImage(0);
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
        solo.waitForActivity(LoginActivity.class, 2000);

        solo.assertCurrentActivity("Wrong activity",LoginActivity.class);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        solo.assertCurrentActivity("Wrong activity",MainActivity.class);

        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("All Habits");

        ArrayList<TextView> list = solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);

        System.out.println("Clicked on: " + list);

        solo.clickOnButton("Edit");

        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        solo.getCurrentActivity();

        EditText title = solo.getEditText(R.id.title);
        EditText reason = solo.getEditText(R.id.reason);
        EditText startDate = solo.getEditText(R.id.startDate);

        solo.enterText(title, "Jogging");
        Assert.assertTrue(solo.searchText("Jogging"));

        solo.enterText(reason, "I like to jog");
        Assert.assertTrue(solo.searchText("I like to jog"));

        solo.enterText(startDate, "20/04/2016");
        Assert.assertTrue(solo.searchText("20/04/2016"));

        solo.clickOnText("Mon");
        solo.isToggleButtonChecked("Mon");
        solo.clickOnText("Wed");
        solo.isToggleButtonChecked("Wed");
        solo.clickOnButton("Save");

        solo.clickOnImage(0);
        solo.clickOnImage(0);
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

        // Now need to click on a habit and test the editing of the habit.

    }
}
