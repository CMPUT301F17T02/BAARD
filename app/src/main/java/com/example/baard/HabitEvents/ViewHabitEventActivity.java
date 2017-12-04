/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.HabitEvents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baard.Controllers.FileController;
import com.example.baard.Controllers.SerializableImage;
import com.example.baard.Controllers.TypefaceSpan;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitEvent;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Activity called when user selects a HabitEvent when viewing all HabitEvents
 * @author amckerna
 * @version 2.0
 * @see AppCompatActivity
 * @see HabitEvent
 * @see AllHabitEventsFragment
 * @since 1.0
 */
public class ViewHabitEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 13.5f;
    private Habit habit;
    private HabitEvent habitEvent;
    private final FileController fileController = new FileController();
    private User user;
    private GoogleMap mMap;
    /**
     * When created, sets the content of all of its fields to match the given HabitEvent.
     * @param savedInstanceState saved state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve Habitevent identifier (date)
        String eventDateString = getIntent().getStringExtra("habitEventDate");

        fetchHabitEvent(eventDateString);

        setContentView(R.layout.activity_view_habit_event);

        setActionBarTitle("View Habit Event");
        changeFont();
    }

    @Override
    public void onStart() {
        super.onStart();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Create Map
        if (habitEvent.getLocation() != null) {
            mapFragment.getView().setVisibility(View.VISIBLE);
            mapFragment.getMapAsync(this);
        } else {
            mapFragment.getView().setVisibility(View.GONE);
        }

        TextView name = (TextView) findViewById(R.id.HabitName);
        name.setText(habitEvent.getHabit().getTitle());
        TextView date = findViewById(R.id.HabitEventDate);
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        date.setText(formatter.format(habitEvent.getEventDate()));
        TextView comment = findViewById(R.id.commentView);
        comment.setText(habitEvent.getComment());
        ImageView image = findViewById(R.id.ImageView);
        // set image if there is one
        //Bitmap bmp = habitEvent.getImage().getBitmap();
        if (habitEvent.getBitmapString() != null) {
            image.setImageBitmap(SerializableImage.getBitmapFromString(habitEvent.getBitmapString()));
        }
        //set onClick listeners for the edit/delete buttons
        Button deleteButton = findViewById(R.id.DeleteHabitEventButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteHabitEvent();
            }
        });
        Button editButton = findViewById(R.id.EditHabitEventButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                editHabitEvent();
            }
        });
    }

    public void changeFont() {
        Typeface ralewayRegular = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        TextView name = (TextView) findViewById(R.id.HabitName);
        TextView date = (TextView) findViewById(R.id.HabitEventDate);
        TextView comment = (TextView) findViewById(R.id.commentView);
        Button editHabitEventButton = (Button) findViewById(R.id.EditHabitEventButton);
        Button deleteHabitEventButton = (Button) findViewById(R.id.DeleteHabitEventButton);

        name.setTypeface(ralewayRegular);
        date.setTypeface(ralewayRegular);
        comment.setTypeface(ralewayRegular);
        editHabitEventButton.setTypeface(ralewayRegular);
        deleteHabitEventButton.setTypeface(ralewayRegular);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ViewHabitEvent", "FLAG0");
        mMap = googleMap;
        mMap.clear();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(habitEvent.getLocation(), DEFAULT_ZOOM));
        mMap.addMarker(new MarkerOptions().position(habitEvent.getLocation()));
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
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
        Intent intent = new Intent(this, EditHabitEventActivity.class);
        intent.putExtra("habitEventDate",habitEvent.getEventDate().toString());
        habit.sendToSharedPreferences(getApplicationContext());
        startActivityForResult(intent,1);
    }
}