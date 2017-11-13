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

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chrygore on 11/11/17.
 */

/**
 * The class for testing the CreateNewHabitEventFragment UI functions.
 *
 * These tests assume that there is a Jogging Habit in the database with a start date of April 20, 2016.
 * Without this, some or all of these tests may fail.
 *
 * @author amckerna
 * @version 1.0
 */
public class CreateHabitEventTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    //note: these tests assume a Habit exists to create a HabitEvent for.
    private Solo solo;
    //private String dateInput;
    private LoginActivity activity;
    public CreateHabitEventTest(){
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
        solo.clickOnImageButton(0);
        // select create new habit event
        solo.clickOnImageButton(0);
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);

        Log.d("SETUP","setUp()");
    }

    /**
     * Create a HabitEvent with valid parameters.
     * @throws Exception
     */
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
        solo.assertCurrentActivity("Should be in ViewHabitEventActivity",ViewHabitEventActivity.class);
        solo.searchText("test comment");
        solo.searchText(habitName);
        // now delete it
        solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
    }

    /**
     * Create a habit event with a date not in the format dd/mm/yyyy
     */
    @Test
    public void testInvalidDateFormat(){
        //select the first item in the spinner (note that this test will fail if there are no habits)
        solo.pressSpinnerItem(0,0);
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "December 25, 2017");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.searchText("Invalid date entry:");
    }

    /**
     * Create a HabitEvent with no date entered.
     */
    @Test
    public void testNoDateEntry(){
        //select the first item in the spinner (note that this test will fail if there are no habits)
        solo.pressSpinnerItem(0,0);
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.searchText("Invalid date entry:");
    }

    /**
     * Create a HabitEvent with a comment longer than 20 characters.
     */
    @Test
    public void testCommentTooLong(){
        //select the first item in the spinner (note that this test will fail if there are no habits)
        solo.pressSpinnerItem(0,0);
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "25/12/2012");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "This comment surely is far too long. It might even have more than 20 characters! Truly shocking...");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.searchText("Comment is too long (20 char max).");
    }

    /**
     * Create a HabitEvent on the same day as another HabitEvent.
     * @throws Exception
     */
    @Test
    public void testCreateHabitEventWithSameDate() throws Exception {
        //select the first item in the spinner (note that this test will fail if there are no habits)
        solo.pressSpinnerItem(0,0);
        Spinner spinner = (Spinner) solo.getView(R.id.habitSpinner);
        String habitName = spinner.getSelectedItem().toString();
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "25/12/2016");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.assertCurrentActivity("Now viewing HabitEvent after creation",ViewHabitEventActivity.class);
        solo.searchText("test comment");
        solo.searchText(habitName);
        //solo.sendKey(KeyEvent.KEYCODE_BACK);
        KeyEvent kdown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        solo.sendKey(kdown.getKeyCode());
        solo.clickOnImageButton(0);
        solo.clickOnText("Habit Event History");
        solo.clickOnImageButton(0);
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        // create another HabitEvent on the same date
        solo.pressSpinnerItem(0,0);
        date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "25/12/2016");
        comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.searchText("A HabitEvent already exists on this date.");
        //now delete it for the sake of other tests
        //solo.sendKey(KeyEvent.KEYCODE_BACK);
        solo.clickOnImageButton(0);
        solo.clickOnText("Habit Event History");
        solo.waitForFragmentById(R.layout.fragment_all_habit_events);
        solo.clickOnText("December 25, 2016");
        solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
    }

    /**
     * Create a HabitEvent before the start date of the Jogging Habit.
     */
    @Test
    public void testCreatingHabitEventBeforeHabitStart(){
        //
        Spinner habitSpinner = (Spinner) solo.getView(R.id.habitSpinner);
        solo.clickOnView(habitSpinner);
        solo.clickOnText("Jogging");
        //Jogging has a start date of April 20, 2016
        EditText date = (EditText) solo.getView(R.id.HabitEventDateEditText);
        solo.enterText(date, "25/01/2016");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.searchText("Date is before habit start date.");
    }

}
