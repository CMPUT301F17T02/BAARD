/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import org.junit.Test;


/**
 * Class for testing the ability to delete habit events.
 *
 * Requires that there is at least one Habit currently present. Otherwise, the tests within may fail.
 *
 * @author amckerna
 * @version 1.0
 */
public class DeleteHabitEventTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    Solo solo;
    Activity activity;
    public DeleteHabitEventTest(){super(LoginActivity.class);}

    /**
     * Sets up the tests in this class. Logs the user out and logs into the test username/password.
     * @throws InterruptedException
     */
    @Override
    public void setUp() throws InterruptedException {
        activity = (LoginActivity) getActivity();
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
        // sign the testing user in
        solo.assertCurrentActivity("Should be in LoginActivity.",LoginActivity.class);
        //solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");
        solo.assertCurrentActivity("Should be in MainActivity.",MainActivity.class);
        Log.d("SETUP","setUp()");
    }

    /**
     * Tests the functionality of the deletion of habit events.
     */
    @Test
    public void testDelete(){
        //create a habit event and delete it

        //first, delete the habitevent if it already exists
        solo.clickOnImageButton(0);
        solo.clickOnText("Habit Event History");
        solo.waitForFragmentById(R.layout.fragment_all_habit_events);
        if (solo.searchText("December 25, 2016") == true) {
            solo.clickOnText("December 25, 2016");
            solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
        }

        solo.clickOnImageButton(0);
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        //select the first item in the spinner (note that this test will fail if there are no habits)
        solo.pressSpinnerItem(0,0);
        Spinner spinner = (Spinner) solo.getView(R.id.habitSpinner);
        String habitName = spinner.getSelectedItem().toString();
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.clickOnView(date);
        solo.setDatePicker(0,2016,11,25);
        solo.clickOnText("OK");
        String dateString = date.getText().toString();
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.assertCurrentActivity("Should be in ViewHabitEventActivity",ViewHabitEventActivity.class);
        solo.searchText("test comment");
        solo.searchText(habitName);
        // now delete it
        solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        solo.clickOnImageButton(0);
        solo.clickOnText("Habit Event History");
        if (solo.searchText("December 25, 2016") == false)
            assertTrue(true);
        else
            fail();
    }

}
