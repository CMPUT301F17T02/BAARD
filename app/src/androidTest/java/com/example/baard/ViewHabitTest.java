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

public class ViewHabitTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    private Solo solo;

    /**
     * @throws DataFormatException
     */
    public ViewHabitTest() throws DataFormatException {
        super(LoginActivity.class);
    }

    /**
     * Sets up the basic of the create new habit test.
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
     * First view test ensure that a habit is properly viewed from all habits screen.
     * @throws Exception
     */
    public void testViewAllHabits() throws Exception {
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
            solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
            solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        }

        // sign the testing user in
        solo.waitForActivity(LoginActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        solo.waitForActivity(MainActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        // Go to all habits
        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

        // See what the first activity saved is
        TextView textFromList = solo.getText(1);
        Log.i("text: ", textFromList.getText().toString());

        // Now need to click on a habit and test the viewing of the habit.
        ArrayList<TextView> list = solo.clickInList(0);
        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        TextView viewHabitName = solo.getText(1);
        Log.i("Clicked on: ", viewHabitName.toString());

        assertTrue(solo.searchText(textFromList.getText().toString()));
        assertEquals(textFromList.getText(), viewHabitName.getText());
    }

    /**
     * Second view test ensure that a habit is properly viewed from creation screen.
     * @throws Exception
     */
    public void testViewHabitFromCreate() throws Exception {
        Activity activity = getActivity();

        // already logged in as previous test case

        solo.clickOnImage(0);
        solo.clickOnText("Create New Habit");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_create_new_habit);

        // enter in all the data to be created
        EditText editTextTitle = (EditText) solo.getView(R.id.title);
        EditText editTextReason = (EditText) solo.getView(R.id.reason);

        String title = "Swimming";
        String reason = "I need to get fit";
        String date = "16/03/2017";

        solo.enterText(editTextTitle, title);
        solo.enterText(editTextReason, reason);
        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        solo.clickOnText("Mon");

        // create the habit
        solo.clickOnButton("Create");

        // check that we are on view now
        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        // Test to see if viewing the correct information
        Assert.assertTrue(solo.searchText(title));
        Assert.assertTrue(solo.searchText(reason));
        Assert.assertTrue(solo.searchText(date));

        // delete this habit so it does not interfere with other test cases
        solo.clickOnButton("Delete");

        solo.waitForActivity(MainActivity.class, 2000);

        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_all_habits);

        // ensure it was deleted on main page
        assertFalse(solo.searchText("Swimming"));

        // To log out
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
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
    }
}
