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
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public LoginActivityTest() {
        super(com.example.baard.LoginActivity.class);

    }

    public void setUp() throws Exception {
        Log.d("SETUP", "First");
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testSignIn() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This field is required", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.username), "Inv@lid Charact#rs");
        solo.clickOnButton("Sign in");
        assertTrue(solo.waitForText("This username contains invalid characters", 1, 1000));

        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.enterText((EditText) solo.getView(R.id.username), "anarten");
        solo.clickOnButton("Sign in");
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.username));
    }

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

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
