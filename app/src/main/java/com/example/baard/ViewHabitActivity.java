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

import static com.example.baard.Day.MONDAY;
import static com.example.baard.Day.TUESDAY;

public class ViewHabitActivity extends AppCompatActivity {

    DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        
        ArrayList<Day> days = new ArrayList<Day>();
        days.add(MONDAY);
        days.add(TUESDAY);
        Habit habit = new Habit("test", "test", new Date(), days);

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

    /* Called when the user taps the Edit button */
    public void editHabit(View view) {
        // TODO pass the habit
        Intent intent = new Intent(this, EditHabitActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the Delete button */
    public void deleteHabit(View view) {
        // TODO delete functionality
        finish();
    }
}
