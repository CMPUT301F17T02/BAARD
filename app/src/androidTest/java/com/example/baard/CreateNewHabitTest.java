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

public class CreateNewHabitTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;
    private CreateNewHabitFragment fragment;

    public CreateNewHabitTest() throws DataFormatException {
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
        fragment = new CreateNewHabitFragment();
        solo = new Solo(getInstrumentation(), activity);
        Log.d("SETUP","setUp()");
    }

    /** Assumed that the user has already been registered */
    public void testCreate() throws Exception {

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
//        solo.waitForActivity(MainActivity.class, 2000);

//        solo.waitForFragmentById(R.layout.fragment_create_new_habit);


        solo.assertCurrentActivity("Wrong activity",LoginActivity.class);
        solo.waitForFragmentById(R.layout.fragment_create_new_habit_event);
        EditText username = (EditText) solo.getView(R.id.username);
        /tText name = (EditText) solo.getView(R.id.name);
        solo.clearEditText(username);
        //solo.clearEditText(name);
        solo.enterText(username, "Andrew.M");
        //solo.enterText(name, "Andrew");
        //Button loginButton = (Button) solo.getView(R.id.sign_in_button);
        solo.clickOnButton("Sign in");

        solo.waitForActivity(ViewHabitActivity.class, 2000);
        //solo.assertCurrentActivity("Wrong activity", MainActivity.class);

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
        solo.isToggleButtonChecked("Mon");
        solo.clickOnButton("Create");

        solo.waitForActivity(ViewHabitActivity.class, 2000);
        //solo.assertCurrentActivity("wrong activity", ViewHabitActivity.class);

        solo.clickOnButton("Delete");

        // To log out
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


}
