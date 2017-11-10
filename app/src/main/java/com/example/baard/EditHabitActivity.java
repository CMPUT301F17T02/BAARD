/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);
    }

    /* Called when the user taps the Save button */
    public void saveHabit(View view) {
        finish();
    }
}
