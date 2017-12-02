/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SettingsActivity extends AppCompatActivity {

    private FileController fc;
    private User user;
    private Button editButton, saveButton;
    private EditText nameEdit;
    private TextView nameView;

    /**
     * Opens up the settings activity, allowing users to see their username and their full name.
     * If they wish, they can then edit their name or delete their acocunt.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        String username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        fc = new FileController();
        user = fc.loadUser(getApplicationContext(), username);

        TextView userNameView = (TextView) findViewById(R.id.username);
        nameView = (TextView) findViewById(R.id.nameV);
        nameEdit = (EditText) findViewById(R.id.name);
        editButton = (Button) findViewById(R.id.edit);
        saveButton = (Button) findViewById(R.id.save);

        nameEdit.setText(user.getName());
        nameView.setText(user.getName());

        userNameView.setText(username);

        getSupportActionBar().setTitle("Settings");
    }

    /**
     * Allows users to completely delete their account locally and online.
     *
     * @param view
     */
    public void delete(View view) {
        Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Allows users to edit their name and see the "save" button if they wish to
     * save those changes.
     *
     * @param view
     */
    public void edit(View view) {
        // show editable fields and save button
        nameEdit.setVisibility(View.VISIBLE);
        nameView.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
        // hide edit Button
        editButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Saves changes to the user's name to the database. Locks the editable field until
     * asked to edit again.
     *
     * @param view
     */
    public void save(View view) {
        // hide editable fields and save button
        nameEdit.setVisibility(View.GONE);
        nameView.setText(nameEdit.getText().toString());
        nameView.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        // show edit button
        editButton.setVisibility(View.VISIBLE);

        user.setName(nameEdit.getText().toString());
        fc.saveUser(getApplicationContext(), user);
    }

}
