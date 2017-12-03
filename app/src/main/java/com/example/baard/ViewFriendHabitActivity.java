/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewFriendHabitActivity extends AppCompatActivity {

    private final DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    User user;
    HabitList habitList;
    Habit habit;
    String username;
    int position;
    FileController fc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_habit);
        fc = new FileController();

        // grab the index of the item in the list
        Bundle extras = getIntent().getExtras();

        position = extras.getInt("HabitPosition");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());
    }

    @Override
    public void onStart() {
        super.onStart();

        // load required data
        //TODO: SET USER TO FRIEND SELECTED
        user = fc.loadUser(getApplicationContext(), username);
        habitList = user.getHabits();
        habit = habitList.getHabit(position);

        // set all of the values for the habit to be viewed
        TextView titleView = (TextView) findViewById(R.id.title_friend);
        TextView reasonView = (TextView) findViewById(R.id.reason_friend);
        TextView startDateView = (TextView) findViewById(R.id.startDate_friend);
        TextView frequencyView = (TextView) findViewById(R.id.frequency_friend);
        titleView.setText(habit.getTitle());
        reasonView.setText(habit.getReason());
        startDateView.setText(formatter.format(habit.getStartDate()));
        frequencyView.setText(habit.getFrequencyString());

        TextView milestoneTextView = (TextView) findViewById(R.id.milestoneTextView_friend);
        int milestone = habit.milestone();

        if (milestone > 0) {
            milestoneTextView.setText("Milestone reached: "+Integer.toString(milestone)+" habit events completed!");
            milestoneTextView.setVisibility(View.VISIBLE);
        } else {
            milestoneTextView.setVisibility(View.GONE);
        }

        TextView streakTextView = (TextView) findViewById(R.id.streakTextView_friend);
        int streak = habit.streak();
        if (streak > 4) {
            streakTextView.setText("This habit is currently on a streak of "+Integer.toString(streak)+"!");
            streakTextView.setVisibility(View.VISIBLE);
        } else {
            streakTextView.setVisibility(View.GONE);
        }

        getSupportActionBar().setTitle("View Habit");
        HabitEvent habitEvent = habit.getEvents().getHabitEvent(0);

        TextView date = (TextView) findViewById(R.id.eventStartDate_friend);
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        date.setText(formatter.format(habitEvent.getEventDate()));
        TextView comment = (TextView) findViewById(R.id.comment_friend);
        comment.setText(habitEvent.getComment());
        ImageView image = (ImageView) findViewById(R.id.friendEventImage);
        // set image if there is one
        //Bitmap bmp = habitEvent.getImage().getBitmap();
        if (habitEvent.getBitmapString() != null) {
            image.setImageBitmap(SerializableImage.getBitmapFromString(habitEvent.getBitmapString()));
        }

    }
}
