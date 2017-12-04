/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by randi on 03/12/17.
 */

public class FriendListTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    private String todayDofW;

    /**
     * Constructor for the FriendListTest Class
     */
    public FriendListTest() {
        super(LoginActivity.class);
    }

    /**
     * Setup function for InstrumentationTest Cases
     */
    @Override
    public void setUp() {
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

        // sign the testing user in
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        EditText username = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username);
        solo.enterText(username, "Andrew.M");
        solo.clickOnButton("Sign in");

        // go to main activity
        solo.waitForActivity(MainActivity.class, 2000);


    }

    public void testFriendsList() {

        solo.clickOnImage(0);
        solo.clickOnText("Explore Friends");

        solo.assertCurrentActivity("wrong acitivty", ExploreFriends.class);
        solo.waitForFragmentById(R.layout.fragment_list_friends, 2000);


    }


}
