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

public class CreateHabitEventTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private MainActivity activity;
    public CreateHabitEventTest(){
        super(MainActivity.class);
    }


    @Override
    public void setUp() throws InterruptedException {
        activity = (MainActivity) getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP","setUp()");
    }

    @Test
    public void testCreateHabitEvent() throws Exception {
        // this test requires that you are already logged in
        solo.assertCurrentActivity("In MainActivity at start of test.",MainActivity.class);
        solo.clickOnImageButton(0);
        // navigate to "Create HabitEvent"
        solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        solo.pressSpinnerItem(0,0);
        Spinner spinner = (Spinner) solo.getView(R.id.habitSpinner);
        String habitName = spinner.getSelectedItem().toString();
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        String dateString = date.getText().toString();
        solo.enterText(date, "25/12/2016");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        //solo.wait(1000);
        solo.assertCurrentActivity("Now viewing HabitEvent after creation",ViewHabitEventActivity.class);
        solo.searchText("test comment");
        solo.searchText(habitName);
        solo.searchText(dateString);
    }

    @Test
    public void testInvalidInput(){
        //solo.something
    }

}
