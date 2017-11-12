/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.DataFormatException;

import static com.example.baard.Day.MONDAY;
import static com.example.baard.Day.TUESDAY;

public class ViewHabitActivity extends AppCompatActivity {

    DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    /**
     * This create method sets the text based on habit retrieved
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        ArrayList<Day> days = new ArrayList<Day>();
        days.add(MONDAY);
        days.add(TUESDAY);
        Habit habit = null;
        try {
            habit = new Habit("test", "test", new Date(), days);
        } catch (DataFormatException e) {
            e.printStackTrace();
        }

        // set all of the values for the habit to be viewed
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView reasonView = (TextView) findViewById(R.id.reason);
        TextView startDateView = (TextView) findViewById(R.id.startDate);
        TextView frequencyView = (TextView) findViewById(R.id.frequency);
        titleView.setText(habit.getTitle());
        reasonView.setText(habit.getReason());
        startDateView.setText(formatter.format(habit.getStartDate()));
        frequencyView.setText(habit.getFrequencyString());
    }

    /**
     * Called when the user taps the Edit button.
     * Sends data for user to edit.
     *
     * @param view
     */
    public void editHabit(View view) {
        // TODO pass the habit
        Intent intent = new Intent(this, EditHabitActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Delete button.
     * Removes habit from user's habit list.
     *
     * @param view
     */
    public void deleteHabit(View view) {
        // TODO delete functionality
        finish();
    }
}
