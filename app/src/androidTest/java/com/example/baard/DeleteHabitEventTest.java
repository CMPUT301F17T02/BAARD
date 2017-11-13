/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.Test;

/**
 * Created by chrygore on 13/11/17.
 */

public class DeleteHabitEventTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    Solo solo;
    Activity activity;
    public DeleteHabitEventTest(){super(LoginActivity.class);}

    @Override
    public void setUp() throws InterruptedException {
        //DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        //dateInput = formatter.format(new Date());
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

    @Test
    public void testDelete(){
        //create a habit event and delete it
        
    }

}
