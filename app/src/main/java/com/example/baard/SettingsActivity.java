/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baard.Controllers.ElasticSearchController;
import com.example.baard.Controllers.FileController;
import com.example.baard.Controllers.TypefaceSpan;
import com.example.baard.Entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Settings for the user's account activity
 * @see MainActivity
 * @see ElasticSearchController
 * @see FileController
 * @author bangotti
 * @since 2.0
 * @version 1.1
 */
public class SettingsActivity extends AppCompatActivity {

    private FileController fc;
    private User user;
    private Button editButton, saveButton;
    private EditText nameEdit;
    private AlertDialog dialog;
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

        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (fc.deleteFile(getApplicationContext())) {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
                            sharedPrefsEditor.clear();
                            sharedPrefsEditor.commit();
                            ElasticSearchController.DeleteUserTask deleteUserTask = new ElasticSearchController.DeleteUserTask();
                            deleteUserTask.execute(user);
                            setResult(RESULT_OK);
                            Toast.makeText(SettingsActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Account could not be deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog
        dialog = builder.create();

        setActionBarTitle(getString(R.string.settings));
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     *  Copied from https://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
     */
    private void setActionBarTitle(String str) {
        String fontPath = "Raleway-Regular.ttf";

        SpannableString s = new SpannableString(str);
        s.setSpan(new TypefaceSpan(this, fontPath), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        getSupportActionBar().setTitle(s);
    }

    /**
     * Allows users to completely delete their account locally and online.
     *
     * @param view
     */
    public void delete(View view) {
        dialog.show();
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
        editButton.setVisibility(View.GONE);
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
        saveButton.setVisibility(View.GONE);
        // show edit button
        editButton.setVisibility(View.VISIBLE);

        user.setName(nameEdit.getText().toString());
        fc.saveUser(getApplicationContext(), user);
    }

    /**
     * Let main activity know not to close down
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

}
