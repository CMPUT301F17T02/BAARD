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
import static org.junit.Assert.*;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

/**
 * Created by randi on 12/11/17.
 */

public class EditHabitTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    /**
     * @throws DataFormatException
     */
    public EditHabitTest() throws DataFormatException {
        super(LoginActivity.class);
    }

    /**
     * Sets up the basic of the edit habit test.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        // get instrumentation get all info from device writing on
        // get activity gets link to lonely twitter
        Activity activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        Log.d("SETUP","setUp()");
    }

    /**
     * Test if editing a habit from view works
     * @throws Exception
     */
    public void testEditHabit() throws Exception {
        Activity activity = getActivity();

        // if already logged in, log out
        if (!(solo.searchButton("Register", true))) {
            solo.clickOnImage(0);
            solo.scrollDown();
            solo.clickOnText("Logout");
        }
        solo.waitForActivity(LoginActivity.class, 2000);

        // Log in
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        solo.waitForActivity(MainActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        // Enter menu
        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("All Habits");

        ArrayList<TextView> list = solo.clickInList(0);
        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        System.out.println("Clicked on: " + list);

        // Start editing
        solo.clickOnButton("Edit");
        solo.waitForActivity(EditHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", EditHabitActivity.class);
        solo.getCurrentActivity();

        EditText title = solo.getCurrentActivity().findViewById(R.id.title);
        EditText reason = solo.getCurrentActivity().findViewById(R.id.reason);
        EditText startDate = solo.getCurrentActivity().findViewById(R.id.startDate);

        solo.clearEditText(title);
        solo.enterText(title, "Jogging");
        Assert.assertTrue(solo.searchText("Jogging"));

        solo.clearEditText(reason);
        solo.enterText(reason, "I like to jog");
        Assert.assertTrue(solo.searchText("I like to jog"));

        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        Assert.assertTrue(solo.searchText("16/03/2017"));

        solo.clickOnText("Mon");
        solo.isToggleButtonChecked("Mon");
        solo.clickOnText("Wed");
        solo.isToggleButtonChecked("Wed");
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);

        // Go back to edit and change the habit back to what it previously was.
        solo.clickOnButton("Edit");

        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        solo.getCurrentActivity();

        solo.clearEditText(title);
        solo.enterText(title, "Walking");
        Assert.assertTrue(solo.searchText("Walking"));

        solo.clearEditText(reason);
        solo.enterText(reason, "I like to walk");
        Assert.assertTrue(solo.searchText("I like to walk"));

        solo.clearEditText(startDate);
        solo.enterText(startDate, "20/11/2017");
        Assert.assertTrue(solo.searchText("20/11/2017"));

        solo.clickOnText("Mon");
        solo.isToggleButtonChecked("Mon");
        solo.clickOnText("Wed");
        solo.isToggleButtonChecked("Wed");
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);

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
