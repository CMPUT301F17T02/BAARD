/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ViewFriendActivity extends AppCompatActivity {

    private int position;
    private String username;
//    private FileController fc;
    private User user;

    /**
     * This create method sets the text based on habit retrieved
     * @param savedInstanceState Bundle for the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        //fc = new FileController();

        // grab the index of the item in the list
        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
//        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

    }

    /**
     * Load the user to display the updated habit
     */
    @Override
    public void onStart() {
        super.onStart();

        // load required data
//        user = fc.loadUser(getApplicationContext(), username);

    }

    /**
     * Ensures the app returns to the proper fragment of main when back pressed
     * @param item the menu item of the toolbar (only home in this case)
     * @return boolean true if success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
