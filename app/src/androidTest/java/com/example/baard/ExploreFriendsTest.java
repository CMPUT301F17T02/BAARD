/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.baard.Friends.ExploreFriends;
import com.example.baard.Friends.FindFriendsFragment;
import com.robotium.solo.Solo;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by randi on 04/12/17.
 */

public class ExploreFriendsTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;
    private String todayDofW;

    /**
     * Constructor for the FriendListTest Class
     */
    public ExploreFriendsTest() {
        super(LoginActivity.class);
    }

    /**
     * Setup function for InstrumentationTest Cases
     */

    public void andrewLogin() {
        solo = new Solo(getInstrumentation(), getActivity());
        // log out if we are logged in for each test
        if (!(solo.searchButton("Register", true))) {
            logOut();
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



    public void testExploreFriends() {

        andrewLogin();

        solo.clickOnImage(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
//        solo.clickOnText("EXPLORE FRIENDS");

        solo.assertCurrentActivity("wrong acitivty", ExploreFriends.class);
        solo.waitForFragmentById(R.layout.fragment_list_friends, 2000);

        assertTrue(solo.searchText("My Friends"));
        assertTrue(solo.searchText("Find Friends"));
        assertTrue(solo.searchText("Friend Requests"));

    }

    public void testSendFriendRequest() {

        andrewLogin();

        testExploreFriends();

        solo.clickOnText("Find Friends");
        solo.waitForFragmentById(R.layout.fragment_find_friends, 2000);

        View view = solo.getText("1cooldude");
        ViewGroup viewGroupContaingTextAndButton = (ViewGroup) view.getParent();
        Button button = (Button) viewGroupContaingTextAndButton.getChildAt(1);
        solo.clickOnView(button);

        solo.waitForFragmentById(R.layout.fragment_find_friends, 2000);

        assertTrue(solo.searchText("Pending"));

        logOut();

    }


    public void testDeclineFriendRequest() {
        solo = new Solo(getInstrumentation(), getActivity());

        if (!(solo.searchButton("Register", true))) {
            logOut();
        }

        solo.waitForActivity(LoginActivity.class);
        EditText username1 = (EditText) solo.getView(R.id.username);
        solo.clearEditText(username1);
        solo.enterText(username1, "friendTest");

        solo.clickOnButton("Sign in");

        testExploreFriends();

        solo.clickOnText("Friend Requests");
        solo.waitForFragmentById(R.layout.fragment_friend_requests, 2000);

        assertTrue(solo.searchText("1cooldude", 1, true, true));

        solo.clickOnText("1cooldude");
        solo.clickOnButton("Decline");

        assertFalse(solo.searchText("1cooldude", 1, true, true));
    }

    private void logOut() {
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

}
