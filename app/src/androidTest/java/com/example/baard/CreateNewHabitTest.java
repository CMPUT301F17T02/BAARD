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
import java.util.zip.DataFormatException;
import com.robotium.solo.Solo;

/**
 * Created by randi on 11/11/17.
 */

public class CreateNewHabitTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;
    private CreateNewHabitFragment fragment;

    /**
     * @throws DataFormatException
     */
    public CreateNewHabitTest() throws DataFormatException {
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
        fragment = new CreateNewHabitFragment();
        solo = new Solo(getInstrumentation(), activity);
        Log.d("SETUP","setUp()");
    }

    /**
     * First creation test ensure that a habit is properly created.
     * This ALSO tests delete so that create test can run properly next time.
     * @assumption the user account has already been registered
     * @author Randi
     * @throws Exception
     */
    public void testCreate1AndDelete() throws Exception {

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
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        // go to main activity
        solo.waitForActivity(MainActivity.class, 2000);

        // select create new habit
        solo.clickOnImage(0);
        solo.clickOnText("Create New Habit");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_create_new_habit);

        // fill out the details for the habit
        EditText title = (EditText) solo.getView(R.id.title);
        EditText reason = (EditText) solo.getView(R.id.reason);

        solo.enterText(title, "Swimming");
        Assert.assertTrue(solo.searchText("Swimming"));

        solo.enterText(reason, "I need to get fit");
        Assert.assertTrue(solo.searchText("I need to get fit"));

        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        Assert.assertTrue(solo.searchText("16/03/2017"));

        solo.clickOnText("Mon");
        Assert.assertTrue(solo.isToggleButtonChecked("Mon"));

        // create the habit
        solo.clickOnButton("Create");

        // ensure the page moved to view for success & detail there
        solo.waitForActivity(ViewHabitActivity.class, 2000);
        solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        assertTrue(solo.searchText("Swimming"));

        // go to main page and check it is in list
        solo.clickOnImage(0);
        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

        solo.waitForFragmentById(R.layout.fragment_all_habits, 2000);
        assertTrue(solo.searchText("Swimming", true));

        // click on it to be deleted (for testing and so that this test can run again
        // as there cannot be two of the same habit in the database)
        solo.clickOnText("Swimming");
        solo.clickOnButton("Delete");

        solo.waitForActivity(MainActivity.class, 2000);

        solo.clickOnImage(0);
//        solo.clickOnText("All Habits");
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_all_habits);

        // ensure it was deleted on main page
        assertFalse(solo.searchText("Swimming"));
    }

    /**
     * Second creation test ensure that a habits must be unique.
     * @assumption the user account has already been registered
     * @author Bianca and Randi
     * @throws Exception
     */
    public void testCreate2Duplicate() throws Exception {

        Activity activity = getActivity();

        // already logged in as previous test case

        // go to main activity
        solo.waitForActivity(MainActivity.class, 2000);

        // select create new habit
        solo.clickOnImage(0);
        solo.clickOnText("Create New Habit");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_create_new_habit);

        // fill out the details for the habit
        EditText title = (EditText) solo.getView(R.id.title);
        EditText reason = (EditText) solo.getView(R.id.reason);

        // should already be in database
        solo.enterText(title, "Jogging");
        Assert.assertTrue(solo.searchText("Jogging"));

        solo.enterText(reason, "I need to get fit");
        Assert.assertTrue(solo.searchText("I need to get fit"));

        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        Assert.assertTrue(solo.searchText("16/03/2017"));

        solo.clickOnText("Mon");
        Assert.assertTrue(solo.isToggleButtonChecked("Mon"));

        // create the habit
        solo.clickOnButton("Create");

        // ensure the page DID NOT move to view
        Assert.assertTrue(solo.searchText("Create New Habit"));
        Assert.assertTrue(solo.searchText("Jogging"));
        // ensure error text was set
        Assert.assertTrue(solo.searchText("Title of habit must be unique!"));

        // go to main page and check it is in list
        solo.clickOnImage(0);
//        solo.clickOnText("All Habits");
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

        solo.waitForFragmentById(R.layout.fragment_all_habits, 2000);

        // assert that jogging is seen only once
        Assert.assertTrue(solo.searchText("Jogging", 1));
    }

    /**
     * Third creation test ensure that a habit cannot have blank entries and that there
     * are error messages displayed.
     * @assumption the user account has already been registered
     * @author Bianca and Randi
     * @throws Exception
     */
    public void testCreate3Blank() throws Exception {

        Activity activity = getActivity();

        // already logged in as previous test case

        // go to main activity
        solo.waitForActivity(MainActivity.class, 2000);

        // select create new habit
        solo.clickOnImage(0);
        solo.clickOnText("Create New Habit");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.fragment_create_new_habit);

        EditText title = (EditText) solo.getView(R.id.title);
        EditText reason = (EditText) solo.getView(R.id.reason);

        // don't fill out toggle buttons & datepicker
        solo.enterText(title, "Running");
        Assert.assertTrue(solo.searchText("Running"));

        solo.enterText(reason, "I need to get fit");
        Assert.assertTrue(solo.searchText("I need to get fit"));

        solo.clickOnButton("Create");
        Assert.assertTrue(solo.searchText("Start date is required!"));
        Assert.assertTrue(solo.searchText("No frequency selected"));

        // don't fill out title
        solo.clickOnText("Mon");
        Assert.assertTrue(solo.isToggleButtonChecked("Mon"));

        solo.clickOnEditText(2);
        solo.setDatePicker(0,2017,2,16);
        solo.clickOnText("OK");
        Assert.assertTrue(solo.searchText("16/03/2017"));

        solo.clearEditText(title);

        solo.clickOnButton("Create");
        Assert.assertTrue(solo.searchText("Title of habit is required!"));

        // don't fill out reason
//        solo.enterText(title, "Running");
//        solo.waitForDialogToClose(2000);
//        Assert.assertTrue(solo.searchText("Running"));
//
//        solo.clearEditText(reason);
//
//        solo.clickOnButton("Create");
//        Assert.assertTrue(solo.searchText("Reason of habit is required!"));

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
