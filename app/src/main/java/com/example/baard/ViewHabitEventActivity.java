/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Activity called when user selects a HabitEvent when viewing all HabitEvents
 * @author amckerna
 * @version 1.0
 */
public class ViewHabitEventActivity extends AppCompatActivity {


    private Habit habit;
    private HabitEvent habitEvent;
    private final FileController fileController = new FileController();
    private User user;
    /**
     * When created, sets the content of all of its fields to match the given HabitEvent.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve Habitevent identifier (date)
        String eventDateString = getIntent().getStringExtra("habitEventDate");

        fetchHabitEvent(eventDateString);

        setContentView(R.layout.activity_view_habit_event);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView name = (TextView) findViewById(R.id.HabitName);
        name.setText(habitEvent.getHabit().getTitle());
        TextView date = (TextView) findViewById(R.id.HabitEventDate);
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        date.setText(formatter.format(habitEvent.getEventDate()));
        TextView comment = (TextView) findViewById(R.id.commentView);
        comment.setText(habitEvent.getComment());
        ImageView image = (ImageView) findViewById(R.id.ImageView);
        // set image if there is one
        if (habitEvent.getImageBitmap() != null) {
            image.setImageBitmap(habitEvent.getImageBitmap());
        }
        //set onClick listeners for the edit/delete buttons
        Button deleteButton = (Button) findViewById(R.id.DeleteHabitEventButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteHabitEvent();
            }
        });
        Button editButton = (Button) findViewById(R.id.EditHabitEventButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                editHabitEvent();
            }
        });
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Runs when returning from editEvent Activity
     * @param requestCode code provided by view activity
     * @param resultCode code provided by edit activity
     * @param data intent passed backwards
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String eventDateString = data.getStringExtra("habitEventDate");
                fetchHabitEvent(eventDateString);
            }
        }
    }

    /**
     * returns the username of the user stored in SharedPreferences
     * @return username
     */
    private String getUsername(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        return gson.fromJson(json, new TypeToken<String>() {}.getType());
    }

    private void fetchHabitEvent(String eventDateString) {
        user = fileController.loadUser(getApplicationContext(), getUsername());
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("currentlyViewingHabit", "");
        Habit loadHabit = gson.fromJson(json, new TypeToken<Habit>() {
        }.getType());

        for (Habit habits : user.getHabits().getArrayList()) {
            if (habits.getTitle().equals(loadHabit.getTitle())) {
                habit = habits;
            }
        }
        for (HabitEvent habitEvents : habit.getEvents().getArrayList()) {
            if (habitEvents.getEventDate().toString().equals(eventDateString)) {
                habitEvent = habitEvents;
                break;
            }
        }
        // set the habit so all methods work properly
        habitEvent.setHabit(habit);
    }

    /**
     * Deletes the viewed HabitEvent from the Habit's HabitEventList and finishes the ViewHabitEventActivity
     */
    private void deleteHabitEvent(){
        //delete this habit event from the Habit's HabitEventList
        habit.getEvents().delete(habitEvent);
        fileController.saveUser(getApplicationContext(), user);
        finish();
    }

    /**
     * Invokes the EditHabitEventActivity
     */
    private void editHabitEvent(){
        //call a new activity for editing this bizz
        Intent intent = new Intent(this, EditHabitEventActivity.class);
        //TODO: PASS HABITEVENT TO VIEWHABITEVENTACTIVITY SOMEHOW
        intent.putExtra("habitEventDate",habitEvent.getEventDate().toString());
        habit.sendToSharedPreferences(getApplicationContext());
        startActivityForResult(intent,1);
    }
}