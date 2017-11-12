/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;
import com.robotium.solo.Solo;

/**
 * Created by randi on 11/11/17.
 */

public class CreateNewHabitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private CreateNewHabitFragment fragment;

    public CreateNewHabitTest() throws DataFormatException {
        super(MainActivity.class);
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
        fragment = new CreateNewHabitFragment();
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP","setUp()");
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
//        solo.assertCurrentActivity("First Activity",MainActivity.class);
        solo.waitForActivity(MainActivity.class, 2000);
//        solo.getCurrentActivity();
//        solo.assertCurrentActivity("Second Activity",LoginActivity.class);
        solo.waitForFragmentById(R.layout.fragment_create_new_habit);
//        EditText userName = (EditText) solo.getView(R.id.username);
//        solo.clearEditText(userName);
//        solo.enterText(userName, "Randi");
//        Button loginButton = (Button) solo.getView(R.id.sign_in_button);
//        loginButton.performClick();
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
        solo.waitForActivity(ViewHabitActivity.class);

    }

    @Test
    public void testSetToggleButtons() throws Exception{

//        MainActivity activity = (MainActivity) solo.getCurrentActivity();
//        ArrayList<Day> toggle = new ArrayList<>();
//        ToggleButton mToggle = (ToggleButton) getActivity().findViewById(R.id.mon);
//        ToggleButton wToggle = (ToggleButton) getActivity().findViewById(R.id.wed);
//
//        mToggle.setChecked(true);
//        wToggle.setChecked(true);
//
//        toggle.add(Day.MONDAY);
//        toggle.add(Day.WEDNESDAY);
//
//        //setToggleButtons(getView());
//
//        System.out.println("toggle: " + toggle);
//        //System.out.println("get freq: " + habit.getFrequency());
//
////        assertEquals(toggle, habit.getFrequency());
//
    }


}
