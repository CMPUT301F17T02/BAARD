/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.baard.Day.MONDAY;
import static com.example.baard.Day.TUESDAY;

public class EditHabitActivity extends AppCompatActivity {

    private Habit habit;
    private EditText editTextTitle, editTextReason, editTextStartDate;
    private ArrayList<Day> frequency;
    DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        // TODO pull real data
        ArrayList<Day> days = new ArrayList<Day>();
        days.add(MONDAY);
        days.add(TUESDAY);
        habit = new Habit("test", "test", new Date(), days);

        // set all of the values for the habit to be edited
        editTextTitle = (EditText) findViewById(R.id.title);
        editTextReason = (EditText) findViewById(R.id.reason);
        editTextStartDate = (EditText) findViewById(R.id.startDate);
        editTextTitle.setText(habit.getTitle());
        editTextReason.setText(habit.getReason());
        editTextStartDate.setText(formatter.format(habit.getStartDate()));
        frequency = habit.getFrequency();
        setToggleButtons();
    }

    public void setToggleButtons() {
        ArrayList<ToggleButton> toggles = new ArrayList<>();
        toggles.add((ToggleButton) findViewById(R.id.sun));
        toggles.add((ToggleButton) findViewById(R.id.mon));
        toggles.add((ToggleButton) findViewById(R.id.tue));
        toggles.add((ToggleButton) findViewById(R.id.wed));
        toggles.add((ToggleButton) findViewById(R.id.thu));
        toggles.add((ToggleButton) findViewById(R.id.fri));
        toggles.add((ToggleButton) findViewById(R.id.sat));
        final Day[] possibleValues  = Day.values();

        for (int i = 0; i < toggles.size(); i++) {
            final int finalI = i;
            toggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!frequency.contains(possibleValues[finalI])) {
                            frequency.add(possibleValues[finalI]);
                        }
                    } else {
                        frequency.remove(possibleValues[finalI]);
                    }
                }
            });

            if (frequency.contains(possibleValues[finalI])) {
                toggles.get(i).setChecked(true);
            }
        }
    }

    public Date convertDate(String stringDate) {
        Date date = null;
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
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
        Date convertedStartDate = convertDate(editTextStartDate.getText().toString());
        if (editTextStartDate.getText().toString().equals("") || convertedStartDate == null){
            editTextStartDate.setError("Start date is required!");
            properEntry = false;
        }
        System.out.println(frequency);
        if (frequency.size() < 1) {
            Toast.makeText(this, "No frequency selected", Toast.LENGTH_SHORT).show();
            properEntry = false;
        }
        if (properEntry) {
            habit.setTitle(editTextTitle.getText().toString());
            habit.setReason(editTextReason.getText().toString());
            habit.setStartDate(convertedStartDate);
            habit.setFrequency(frequency);
            commitEdits();
            finish();
        }
    }
}
