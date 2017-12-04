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
 * Implements testing for the Login Activity
 * @see android.test.ActivityInstrumentationTestCase2
 * @see LoginActivity
 * @author anarten
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    /**
     * Constructor for the LoginActivityTest Class
     */
    public LoginActivityTest() {
        super(com.example.baard.LoginActivity.class);

    }

    /**
     * Setup function for InstrumentationTest Cases
     * @throws Exception
     */
    public void setUp() throws Exception {
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
        Log.d("SETUP", "setUp()");
    }

    /**
     * Test Case to test starting the activity
     * @throws Exception
     */
    @Test
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Test case for a username with invalid characters
     */
    @Test
    public void testInvalidUsername() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "Inv@lid Charact#rs");
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This username contains invalid characters", 1, 1000));

        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.name), "Test Name");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This username contains invalid characters", 1, 1000));
    }


    /**
     * Test case for registering a new user
     */
    @Test
    public void testRegister() throws InterruptedException {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This field is required", 2, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "test_username");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.name), "Test Name");
        solo.clickOnButton("Register");
        solo.waitForActivity(MainActivity.class, 3000);
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.sendKey(Solo.MENU);
        solo.sendKey(KeyEvent.KEYCODE_MENU);
        solo.clickOnMenuItem("Settings");

        solo.assertCurrentActivity("wrong_activity",  SettingsActivity.class);
        solo.clickOnButton("Delete");
        solo.waitForDialogToOpen(2000);
        solo.clickOnButton("Delete");

        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
    }

    /**
     * Test case to test signing in a user and all associated errors
     */
    @Test
    public void testSignIn() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "test_username");
        solo.clickOnButton("Sign in");
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

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
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
    }


    /**
     * Test case to test when a username does not exist
     */
    @Test
    public void testUsernameNotExist() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "iDontExist");
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This username does not exist", 1, 1000));
    }

    /**
     * tearDown Function for InstrumentationTest Cases
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
