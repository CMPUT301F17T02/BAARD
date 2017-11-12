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
import android.widget.ToggleButton;
import android.view.View;

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

    private MainActivity testingActivity;
    private CreateNewHabitFragment testFragment;
    private Solo solo;

    Date startDate = new Date();
    ArrayList<Day> frequency = new ArrayList<>();
    Habit habit = new Habit("title", "Reason", startDate, frequency);

    public CreateNewHabitTest() throws DataFormatException {
        super(MainActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        // get instrumentation get all info from device writing on
        // get activity gets link to lonely twitter
        super.setUp();
        testingActivity = getActivity();
        testFragment = new CreateNewHabitFragment();

        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP","setUp()");
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();

    }

    @Test
    public void testSetToggleButtons() {
        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        ArrayList<Day> toggle = new ArrayList<>();
        ToggleButton mToggle = (ToggleButton) getActivity().findViewById(R.id.mon);
        ToggleButton wToggle = (ToggleButton) getActivity().findViewById(R.id.wed);

        mToggle.setChecked(true);
        wToggle.setChecked(true);

        toggle.add(Day.MONDAY);
        toggle.add(Day.WEDNESDAY);

        //setToggleButtons(getView());

        System.out.println("toggle: " + toggle);
        System.out.println("get freq: " + habit.getFrequency());

        assertEquals(toggle, habit.getFrequency());

    }

    private void startFragment( Fragment fragment ) {
        android.support.v4.app.FragmentManager fragmentManager = testingActivity.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null );
        fragmentTransaction.commit();
    }
}
