/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by Adam on 11/11/2017.
 * Implements testing for the Login Activity
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
        Log.d("SETUP", "setUp()");
    }

    /**
     * Test Case to test starting the activity
     * @throws Exception
     */
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Test case for registering a user and all associated errors
     */
    public void testRegister() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "Inv@lid Charact#rs");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.name), "Test Name");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("This username contains invalid characters", 1, 1000));

        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "test_username");

        solo.clickOnButton("Register");
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.clearEditText((EditText) solo.getView(R.id.name));
    }

    /**
     * Test case to test signing in a user and all associated errors
     */
    public void testSignIn() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "Inv@lid Charact#rs");
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This username contains invalid characters", 1, 1000));

        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "test_username");
        solo.clickOnButton("Sign in");
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.username));
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
