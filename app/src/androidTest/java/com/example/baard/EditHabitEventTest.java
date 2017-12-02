/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import org.junit.Assert;
import org.junit.Test;

import java.util.zip.DataFormatException;
import com.robotium.solo.Solo;

/**
 * A class for testing the ability to edit existing habit events.
 * Note that this class does not test the ability to edit images, since solo/robotium cannot navigate Android system UI.
 * @author amckerna
 * @version 1.0
 */

public class EditHabitEventTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    private Solo solo;
    //private String dateInput;
    private LoginActivity activity;
    public EditHabitEventTest(){
        super(LoginActivity.class);
    }

    /**
     * Runs before every test. If the user is logged in, log them out and log them back in with the
     * test username.
     * @throws InterruptedException
     */
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

        // delete habitevent on December 25, 2016 if it exists
        solo.clickOnImageButton(0);
        solo.clickOnText("Habit Event History");
        solo.waitForFragmentById(R.layout.fragment_all_habit_events);
        if (solo.searchText("December 25, 2016") == true) {
            solo.clickOnText("December 25, 2016");
            solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
        }
        //solo.clickOnImageButton(0);
        // select create new habit event
        solo.clickOnImageButton(0);
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        Log.d("SETUP","setUp()");
    }

    @Test
    public void testEditComment(){

    }

    @Test
    public void testEditTooLongComment(){

    }

    @Test
    public void testEditDate(){

    }

    @Test
    public void testEditFutureDate(){

    }

    @Test
    public void testEditDateBeforeHabitStart(){

    }

    @Test
    public void testEditDateToSameDateAsAnotherEvent(){

    }

}
