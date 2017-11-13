/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

/**
 * Activity called when user selects a HabitEvent when viewing all HabitEvents
 */
public class ViewHabitEventActivity extends AppCompatActivity {

    /**
     * When created, sets the content of all of its fields to match the given HabitEvent.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pass this habitevent via intent
        String eventDateString = getIntent().getStringExtra("habitEventDate");
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("currentlyViewingHabit", "");
        Habit habit = gson.fromJson(json, new TypeToken<Habit>() {}.getType());
        HabitEvent habitEvent = null;

        for (HabitEvent habitEvent1: habit.getEvents().getArrayList()){
            if (habitEvent1.getEventDate().toString().equals(eventDateString)){
                habitEvent = habitEvent1;
                break;
            }
        }
        // set the habit so all methods work properly
        habitEvent.setHabit(habit);
        setContentView(R.layout.activity_view_habit_event);

        TextView name = (TextView) findViewById(R.id.HabitName);
        name.setText(habitEvent.getHabit().getTitle());
        TextView date = (TextView) findViewById(R.id.HabitEventDate);
        date.setText(habitEvent.getEventDate().toString());
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

    /**
     * Deletes the viewed HabitEvent from the Habit's HabitEventList
     */
    private void deleteHabitEvent(){
        //delete this habit event from the Habit's HabitEventList
    }

    /**
     * Invokes the EditHabitEventActivity
     */
    private void editHabitEvent(){
        //call a new activity for editing this bizz
    }
}
