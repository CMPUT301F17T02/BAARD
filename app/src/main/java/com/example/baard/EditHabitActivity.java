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
import java.util.zip.DataFormatException;

import static com.example.baard.Day.MONDAY;
import static com.example.baard.Day.TUESDAY;

public class EditHabitActivity extends AppCompatActivity {

    private Habit habit;
    private EditText editTextTitle, editTextReason, editTextStartDate;
    private ArrayList<Day> frequency;
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    /**
     * This create method sets the text and toggle buttons based on habit retrieved
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        // TODO pull real data
        ArrayList<Day> days = new ArrayList<Day>();
        days.add(MONDAY);
        days.add(TUESDAY);
        try {
            habit = new Habit("test", "test", new Date(), days);
        } catch (DataFormatException e) {
            e.printStackTrace();
        }

        // set all of the values for the habit to be edited
        editTextTitle = (EditText) findViewById(R.id.title);
        editTextReason = (EditText) findViewById(R.id.reason);
        editTextStartDate = (EditText) findViewById(R.id.startDate);
        editTextTitle.setText(habit.getTitle());
        editTextReason.setText(habit.getReason());
        editTextStartDate.setText(formatter.format(habit.getStartDate()));
        frequency = habit.getFrequency();
        // set the toggle buttons for the days of the week
        setToggleButtons();
    }

    /**
     * Sets the required toggle buttons for the days of the week.
     * This thereby controls the frequency array to which habits should repeat on.
     */
    public void setToggleButtons() {
        // store all buttons in order of days in the Day enum
        ArrayList<ToggleButton> toggles = new ArrayList<>();
        toggles.add((ToggleButton) findViewById(R.id.sun));
        toggles.add((ToggleButton) findViewById(R.id.mon));
        toggles.add((ToggleButton) findViewById(R.id.tue));
        toggles.add((ToggleButton) findViewById(R.id.wed));
        toggles.add((ToggleButton) findViewById(R.id.thu));
        toggles.add((ToggleButton) findViewById(R.id.fri));
        toggles.add((ToggleButton) findViewById(R.id.sat));
        // grab all possible enum values of Day
        final Day[] possibleValues  = Day.values();

        // iterate through all the toggle buttons to set the listener
        for (int i = 0; i < toggles.size(); i++) {
            final int finalI = i;
            toggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // if the Day is not in the frequency already, add it
                        if (!frequency.contains(possibleValues[finalI])) {
                            frequency.add(possibleValues[finalI]);
                        }
                    } else {
                        // remove the Day from the frequency
                        frequency.remove(possibleValues[finalI]);
                    }
                }
            });

            // based on the habit grabbed, set the toggle to true if it was previously selected
            if (frequency.contains(possibleValues[finalI])) {
                toggles.get(i).setChecked(true);
            }
        }
    }

    /**
     * Function that saves the new list into the file & online
     */
    private void commitEdits() {
        //json = gson.toJson(habitList);
        // TODO functionality of saving
    }

    /**
     * Converts the date from the input of a string format to a date format.
     *
     * @param stringDate
     * @return
     */
    public Date convertDate(String stringDate) {
        Date date = null;
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Called when the user taps the Save button
     *
     * @param view
     * @throws DataFormatException
     */
    public void saveHabit(View view) throws DataFormatException {
        Boolean properEntry = true;

        // throw errors if the user does not input into the mandatory fields
        if (editTextTitle.getText().toString().equals("")) {
            editTextTitle.setError("Title of habit is required!");
            properEntry = false;
        }
        if (editTextReason.getText().toString().equals("")){
            editTextReason.setError("Reason for habit is required!");
            properEntry = false;
        }
        Date convertedStartDate = convertDate(editTextStartDate.getText().toString());
        if (convertedStartDate == null){
            editTextStartDate.setError("Start date is required!");
            properEntry = false;
        }
        System.out.println(frequency);
        if (frequency.size() < 1) {
            Toast.makeText(this, "No frequency selected", Toast.LENGTH_SHORT).show();
            properEntry = false;
        }

        // if all of the values are entered try to save
        if (properEntry) {
            try {
                habit.setTitle(editTextTitle.getText().toString());
                habit.setReason(editTextReason.getText().toString());
                habit.setStartDate(convertedStartDate);
                habit.setFrequency(frequency);
                commitEdits();
                finish();
            } catch (DataFormatException errMsg) {
                // occurs when title or reason are above their character limits
                Toast.makeText(this, errMsg.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}