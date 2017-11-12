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
    public CreateHabitEventTest(){
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws InterruptedException {
        //wait(500000);
        Activity activity = getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP","setUp()");
    }

    @Test
    public void testStart() throws Exception {
        Activity activity = getActivity();
        solo.assertCurrentActivity("yay",LoginActivity.class);
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText userName = (EditText) solo.getView(R.id.username);
        solo.clearEditText(userName);
        solo.enterText(userName, "Andrew");
        Button loginButton = (Button) solo.getView(R.id.sign_in_button);
        loginButton.performClick();
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN); // select first item
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER); // press the first item
    }

    @Test
    public void testValidInput(){
        //solo.something
    }

}
