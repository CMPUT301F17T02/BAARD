/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;
import com.robotium.solo.Solo;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.Before;

/**
 * Created by chrygore on 11/11/17.
 */

public class CreateHabitEventTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;
    CreateHabitEventTest(){
        super(CreateNewHabitEventFragment.class);
    }

    public void setUp() throws Exception {
        // get instrumentation get all info from device writing on
        // get activity gets link to lonely twitter
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP","setUp()");
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();

    }

    public void testValidInput(){
        //solo.something
    }

}
