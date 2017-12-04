/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Spinner;
import org.junit.Test;

import com.example.baard.HabitEvents.EditHabitEventActivity;
import com.example.baard.HabitEvents.ViewHabitEventActivity;
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
            solo.clickOnText("View");
            solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
        }
        //solo.clickOnImageButton(0);
        // select create new habit event
        solo.clickOnImageButton(0);
        solo.clickOnImageButton(0);
        //create a habit event for editing
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        solo.pressSpinnerItem(0,0);
        Spinner spinner = (Spinner) solo.getView(R.id.habitSpinner);
        String habitName = spinner.getSelectedItem().toString();
        solo.clickOnView(solo.getView(R.id.HabitEventDateEditText));
        solo.setDatePicker(0,2016,11,25);
        solo.clickOnText("OK");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.assertCurrentActivity("Should be in ViewHabitEventActivity",ViewHabitEventActivity.class);
        Log.d("SETUP","setUp()");
    }

    @Test
    public void testEditComment(){
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        EditText comment = (EditText) solo.getView(R.id.EditCommentEditText);
        solo.clearEditText(comment);
        solo.enterText(comment, "Edited comment");
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.assertCurrentActivity("Should be in View Habit Event Activity", ViewHabitEventActivity.class);
        solo.searchText("Edited comment");
    }

    @Test
    public void testEditTooLongComment(){
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        EditText comment = (EditText) solo.getView(R.id.EditCommentEditText);
        solo.clearEditText(comment);
        solo.enterText(comment, "This comment is, similar to the comment used in other tests, much too long for a habit event. C'mon guys, what were you thinking???");
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.searchText("Comment is too long (20 char max).");
        solo.assertCurrentActivity("Should still be in edit habit event activity", EditHabitEventActivity.class);
    }

    @Test
    public void testEditDate(){
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        solo.clickOnView(solo.getView(R.id.dateEditText));
        solo.setDatePicker(0,2016,11,15);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.assertCurrentActivity("Should be in View Habit Event Activity", ViewHabitEventActivity.class);
        solo.searchText("December 15, 2016");
        //delete here since we don't check for this date normally
        solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
    }

    @Test
    public void testEditFutureDate(){
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        solo.clickOnView(solo.getView(R.id.dateEditText));
        solo.setDatePicker(0,2018,11,25);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.searchText("Invalid date entry");
        solo.assertCurrentActivity("Should still be in editHabitEventActivity", EditHabitEventActivity.class);
    }

    @Test
    public void testEditDateBeforeHabitStart(){
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        solo.clickOnView(solo.getView(R.id.dateEditText));
        solo.setDatePicker(0,2010,11,25);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.searchText("Invalid date entry");
        solo.assertCurrentActivity("Should still be in editHabitEventActivity", EditHabitEventActivity.class);
    }

    @Test
    public void testEditDateToSameDateAsAnotherEvent(){
        // create another habit, then edit it to the same date as the one created at the start of every test
        // (Dec 25, 2016)
        //solo.goBack();
        solo.clickOnImageButton(0);
        solo.waitForText("Habit Event History");
        solo.clickOnImageButton(0);
        //create a habit event for editing
        solo.waitForText("Create New Habit Event");
        solo.clickOnText("Create New Habit Event");
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        solo.pressSpinnerItem(0,0);
        Spinner spinner = (Spinner) solo.getView(R.id.habitSpinner);
        String habitName = spinner.getSelectedItem().toString();
        solo.clickOnView(solo.getView(R.id.HabitEventDateEditText));
        solo.setDatePicker(0,2016,11,15);
        solo.clickOnText("OK");
        EditText comment = (EditText) solo.getView(R.id.commentEditText);
        solo.clearEditText(comment);
        solo.enterText(comment, "test comment");
        solo.clickOnView(solo.getView(R.id.saveButton));
        solo.assertCurrentActivity("Should be in ViewHabitEventActivity",ViewHabitEventActivity.class);
        //now edit this habit event to Dec 25
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        solo.clickOnView(solo.getView(R.id.dateEditText));
        solo.setDatePicker(0,2016,11,25);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.searchText("A HabitEvent already exists on this date.");
        solo.assertCurrentActivity("Should still be in editHabitEventActivity", EditHabitEventActivity.class);
        //now go back and delete the Dec 15 habit event
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.DeleteHabitEventButton));
    }

    @Test
    public void testEditLocation() throws InterruptedException{
        solo.clickOnView(solo.getView(R.id.EditHabitEventButton));
        solo.clickOnButton("Location");
        solo.waitForView(R.id.save);
        solo.clickOnView(solo.getView(R.id.save));
        solo.assertCurrentActivity("Should be back in EditHabitEventFragment", EditHabitEventActivity.class);
        solo.waitForView(R.id.saveChangesButton);
        solo.clickOnView(solo.getView(R.id.saveChangesButton));
        solo.assertCurrentActivity("Should be in View Habit Event Activity", ViewHabitEventActivity.class);
        solo.searchText("Location Added");
    }

    @Test
    public void testEnterEditFromHabitEventHistory(){
        solo.goBack();
        solo.waitForText("Habit Event History");
        //solo.clickOnImageButton(0);
        //create a habit event for editing
        //solo.clickOnText("Habit Event History");
        //solo.waitForText("December 25, 2016");
        solo.clickOnText("December 25, 2016");
        solo.clickOnText("Edit");
        solo.assertCurrentActivity("Should be in EditHabitEventActivity", EditHabitEventActivity.class);
    }

}
