/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

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
        MainActivity activity = (MainActivity) getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP","setUp()");
    }

    @Test
    public void testCreateHabitEvent() throws Exception {
        //Activity activity = getActivity();

        solo.assertCurrentActivity("yay",MainActivity.class);
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER); // press the first item
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "25/12/2016");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        //solo.clickOnButton("Add Image"); // successfully pressed button, not sure how to navigate through file system

    }

    @Test
    public void testInvalidInput(){
        //solo.something
    }

}
