/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static com.example.baard.Day.MONDAY;
import static com.example.baard.Day.TUESDAY;

public class EditHabitActivity extends AppCompatActivity {

    private Habit habit;
    private EditText editTextTitle, editTextReason, editTextStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        // TODO pull real data
        ArrayList<Day> days = new ArrayList<Day>();
        days.add(MONDAY);
        days.add(TUESDAY);
        Habit habit = new Habit("test", "test", new Date(), days);

        // set all of the values for the habit to be edited
        editTextTitle = (EditText) findViewById(R.id.title);
        editTextReason = (EditText) findViewById(R.id.reason);
        editTextStartDate = (EditText) findViewById(R.id.startDate);
        editTextTitle.setText(habit.getTitle());
        editTextReason.setText(habit.getReason());
        editTextStartDate.setText(habit.getStartDate().toString());
        setToggleButtons(habit.getFrequency());
    }

    public void setToggleButtons(ArrayList<Day> days) {
        ArrayList<ToggleButton> toggles = new ArrayList<>();
        toggles.add((ToggleButton) findViewById(R.id.mon));
        toggles.add((ToggleButton) findViewById(R.id.tue));
        toggles.add((ToggleButton) findViewById(R.id.wed));
        toggles.add((ToggleButton) findViewById(R.id.thu));
        toggles.add((ToggleButton) findViewById(R.id.fri));
        toggles.add((ToggleButton) findViewById(R.id.sat));
        toggles.add((ToggleButton) findViewById(R.id.sun));

        for (ToggleButton tog : toggles) {
            tog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // TODO The toggle is enabled
                    } else {
                        // TODO The toggle is disabled
                    }
                }
            });
        }

        //TODO set it on or off depending on arraylist state!!
    }

    /* Function that saves the new list into file & online */
    private void commitEdits() {
        //json = gson.toJson(habitList);
        // TODO functionality of saving
    }

    /* Called when the user taps the Save button */
    public void saveHabit(View view) {
        Boolean properEntry = true;

        // throw errors if the user does not input into the mandatory fields (name and counters)
        if (editTextTitle.getText().toString().equals("")) {
            editTextTitle.setError("Title of habit is required!");
            properEntry = false;
        }
        if (editTextReason.getText().toString().equals("")){
            editTextReason.setError("Reason for habit is required!");
            properEntry = false;
        }
        if (editTextStartDate.getText().toString().equals("")){
            editTextStartDate.setError("Start date is required!");
            properEntry = false;
        }

        if (properEntry) {
            habit.setTitle(editTextTitle.getText().toString());
            habit.setReason(editTextReason.getText().toString());
            habit.setStartDate(new Date(editTextStartDate.getText().toString()));
            //TODO call for toggles into frequency
            commitEdits();
            finish();
        }

    }
}
