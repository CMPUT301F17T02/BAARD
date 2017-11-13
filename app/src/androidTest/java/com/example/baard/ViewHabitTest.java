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

    public ViewHabitTest() throws DataFormatException {
        super(LoginActivity.class);
    }
//
//    Date startDate = new Date();
//    ArrayList<Day> frequency = new ArrayList<>();
//    Habit habit = new Habit("title", "Reason", startDate, frequency);



    @Override
    protected void setUp() throws Exception {
        // get instrumentation get all info from device writing on
        // get activity gets link to lonely twitter
        Activity activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        Log.d("SETUP","setUp()");
    }

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

        solo.waitForActivity(LoginActivity.class, 2000);

        solo.assertCurrentActivity("Wrong activity",LoginActivity.class);
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        solo.waitForActivity(MainActivity.class, 2000);

        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("All Habits");

        TextView textFromList = solo.getText(1);
        System.out.println("text: " + textFromList.getText());

        // Now need to click on a habit and test the viewing of the habit.
        ArrayList<TextView> list = solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);

        TextView viewHabitName = solo.getText(1);
        System.out.println("Clicked on: " + viewHabitName);

        assertTrue(solo.searchText(textFromList.getText().toString()));
        assertEquals(textFromList.getText(), viewHabitName.getText());

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

    }

    public void testViewHabitFromCreate() throws Exception {
        Activity activity = getActivity();
        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("Create New Habit");
        solo.getCurrentActivity() .getFragmentManager() .findFragmentById(R.layout.fragment_create_new_habit);

        EditText title = (EditText) solo.getView(R.id.title);
        EditText reason = (EditText) solo.getView(R.id.reason);
        EditText startDate = (EditText) solo.getView(R.id.startDate);

        solo.enterText(title, "Swimming");
        Assert.assertTrue(solo.searchText("Swimming"));
        solo.enterText(reason, "I need to get fit");
        Assert.assertTrue(solo.searchText("I need to get fit"));
        solo.enterText(startDate, "20/11/2017");
        Assert.assertTrue(solo.searchText("20/11/2017"));

        solo.clickOnText("Mon");
        solo.clickOnButton("Create");

        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        //Test to see if viewing the correct stuff.g

    }
}
