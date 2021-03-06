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

import com.example.baard.Habits.EditHabitActivity;
import com.example.baard.Habits.ViewHabitActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

/**
 * Created by randi on 12/11/17.
 */

public class EditHabitTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    /**
     * @throws DataFormatException
     */
    public EditHabitTest() throws DataFormatException {
        super(LoginActivity.class);
    }

    /**
     * Sets up the basic of the edit habit test.
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
     * Test if editing a habit from view works
     * @throws Exception
     */
    public void testEditHabit() throws Exception {
        Activity activity = getActivity();

        // if already logged in, log out to ensure we are on TEST user
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

        // Enter menu
        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.clickOnText("All Habits");


        // See what the first activity saved is
        TextView textFromList = solo.getText(1);
        Log.i("text: ", textFromList.getText().toString());

        // Now need to click on a habit and test the viewing of the habit.
        ArrayList<TextView> list = solo.clickInList(0);
        solo.clickOnButton("View");
        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        TextView viewHabitName = solo.getText(1);
        Log.i("Clicked on: ", viewHabitName.toString());

        // Start editing
        solo.clickOnButton("Edit");
        solo.waitForActivity(EditHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", EditHabitActivity.class);
        solo.getCurrentActivity();

//        EditText editTextTitle = (EditText) solo.getView(R.id.title);
//        EditText editTextReason = (EditText) solo.getView(R.id.reason);
//        EditText editTextStartDate = (EditText) solo.getView(R.id.startDate);
        String title = "Running";
        String reason = "I like to run";
        String date = "16/03/2017";

        solo.clearEditText(0);
        solo.enterText(0, title);
        assertTrue(solo.searchText(title));

        solo.clearEditText(1);
        solo.enterText(1, reason);
        assertTrue(solo.searchText(reason));

        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        assertTrue(solo.searchText(date));

        solo.clickOnText("Mon");
        solo.isToggleButtonChecked("Mon");
        solo.clickOnText("Wed");
        solo.isToggleButtonChecked("Wed");

        // save the completed edits
        solo.clickOnButton("Save");

        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.getCurrentActivity();

        // ensure that the edits stuck
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Running");
        solo.clickOnButton("View");
        assertTrue(solo.searchText(title, 1, true, true));
        assertTrue(solo.searchText(reason, 1, true, true));
        assertTrue(solo.searchText("March 16, 2017", 1, true, true));

        // Go back to edit and change the habit back to what it previously was.
        solo.clickOnButton("Edit");

        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        solo.getCurrentActivity();

        solo.clearEditText(0);
        solo.enterText(0, "Jogging");
        assertTrue(solo.searchText("Jogging"));

        solo.clearEditText(1);
        solo.enterText(1, "I like to jog");
        assertTrue(solo.searchText("I like to jog"));

        solo.clearEditText(2);
        solo.setDatePicker(0,2016,3,20);
        solo.clickOnText("OK");
        assertTrue(solo.searchText("20/04/2016"));

        solo.clickOnButton("Save");

        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);

        // log out for other tests
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
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

    }
}
