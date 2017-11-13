/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewHabitActivity extends AppCompatActivity {

    private DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private int position;
    private String username;
    private HabitList habitList;
    private Habit habit;
    private FileController fc;
    private User user;

    /**
     * This create method sets the text based on habit retrieved
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        fc = new FileController();

        // grab the index of the item in the list
        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());
    }


    /**
     * Load user
     */
    @Override
    public void onStart() {
        super.onStart();

        // load required data
        user = fc.loadUser(getApplicationContext(), username);
        habitList = user.getHabits();
        Log.d("elasticSearch", Integer.toString(habitList.size()));
        habit = habitList.getHabit(position);

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
        Intent intent = new Intent(this, EditHabitActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Delete button.
     * Removes habit from user's habit list.
     *
     * @param view
     */
    public void deleteHabit(View view) {
        habitList.delete(habit);
        fc.saveUser(getApplicationContext(), user);
        finish();
    }
}
