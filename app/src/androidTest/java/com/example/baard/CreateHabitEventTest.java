/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.security.keystore.KeyNotYetValidException;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import org.junit.Test;

/**
 * Created by chrygore on 11/11/17.
 */

public class CreateHabitEventTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;
    private LoginActivity activity;
    public CreateHabitEventTest(){
        super(LoginActivity.class);
    }


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
        solo.assertCurrentActivity("Login activity at start.",LoginActivity.class);
        //solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");
        solo.assertCurrentActivity("In MainActivity after logging in.",MainActivity.class);
        solo.clickOnImageButton(0);
        // select create new habit event
        solo.clickOnImageButton(0);
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);

        Log.d("SETUP","setUp()");
    }



    @Test
    public void testCreateHabitEvent() throws Exception {

        //select the first item in the spinner (note that this test will fail if there are no habits)
        solo.pressSpinnerItem(0,0);
        Spinner spinner = (Spinner) solo.getView(R.id.habitSpinner);
        String habitName = spinner.getSelectedItem().toString();
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "25/12/2016");
        String dateString = date.getText().toString();
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.assertCurrentActivity("Now viewing HabitEvent after creation",ViewHabitEventActivity.class);
        solo.searchText("test comment");
        solo.searchText(habitName);
        solo.searchText(dateString);
    }

    @Test
    public void testInvalidDateFormat(){
        //solo.something
        solo.pressSpinnerItem(0,0);
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "December 25, 2017");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        //solo.searchText();

    }

}
