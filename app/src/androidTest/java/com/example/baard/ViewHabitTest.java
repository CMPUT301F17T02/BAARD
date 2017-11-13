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
            solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        }

        // sign the testing user in
        solo.waitForActivity(LoginActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        solo.waitForActivity(MainActivity.class, 2000);

        // Go to all habits
        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("All Habits");

        // See what the first activity saved is
        TextView textFromList = solo.getText(1);
        System.out.println("text: " + textFromList.getText());

        // Now need to click on a habit and test the viewing of the habit.
        ArrayList<TextView> list = solo.clickInList(0);
        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        TextView viewHabitName = solo.getText(1);
        System.out.println("Clicked on: " + viewHabitName);

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
//        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
//        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
//        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
//        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
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
        solo.clickOnButton("Create");

        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        //Test to see if viewing the correct stuff
        Assert.assertTrue(solo.searchText(title));
        Assert.assertTrue(solo.searchText(reason));
        Assert.assertTrue(solo.searchText(date));

        solo.clickOnButton("Delete");
        solo.waitForActivity(MainActivity.class, 2000);

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
