/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Friends;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baard.Controllers.FileController;
import com.example.baard.Controllers.SerializableImage;
import com.example.baard.Controllers.TypefaceSpan;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitEvent;
import com.example.baard.Entities.HabitList;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.view.View.VISIBLE;

/**
 * When the user selects one of their friends and subsequently, one of their friend's habits,
 * this view provides them a snapshot of the habit including the most recent habit event from
 * that user.
 * @author bangotti, rderbysh, amckerna
 * @since 1.0
 */
public class ViewFriendHabitActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private HabitList habitList;
    private Habit habit;
    private HabitEvent habitEvent;
    private String friendUsername;
    private int position;
    private FileController fc;

    private static final float DEFAULT_ZOOM = 13.5f;
    private GoogleMap mMap;

    /**
     * Grabs the position of the user in the list from the previous intent
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_habit);
        fc = new FileController();

        // grab the index of the item in the list
        Bundle extras = getIntent().getExtras();

        position = extras.getInt("HabitPosition");
        friendUsername = extras.getString("FriendUsername");

        setActionBarTitle("View Habit");
        changeFont();
    }

    /**
     * Sets up the entire view and display of the habit and its most recent habit event.
     */
    @Override
    public void onStart() {
        super.onStart();

        // load required data
        User friendUser = fc.loadUserFromServer(friendUsername);
        habitList = friendUser.getHabits();
        habit = habitList.getHabit(position);

        // set all of the values for the habit to be viewed
        TextView titleView = (TextView) findViewById(R.id.title_friend);
        TextView reasonView = (TextView) findViewById(R.id.reason_friend);
        TextView startDateView = (TextView) findViewById(R.id.startDate_friend);
        TextView frequencyView = (TextView) findViewById(R.id.frequency_friend);
        titleView.setText(habit.getTitle());
        reasonView.setText(habit.getReason());
        startDateView.setText(formatter.format(habit.getStartDate()));
        frequencyView.setText(habit.getFrequencyString());

        TextView milestoneTextView = findViewById(R.id.milestoneTextView);
        int milestone = habit.milestone();
        if (milestone > 0) {
            milestoneTextView.setText("Milestone reached: \n"+Integer.toString(milestone)+" habit events completed!");
            milestoneTextView.setVisibility(VISIBLE);
        } else {
            milestoneTextView.setVisibility(View.GONE);
        }

        TextView streakTextView = findViewById(R.id.streakTextView);
        int streak = habit.streak();
        if (streak > 4) {
            streakTextView.setText("Current Streak: "+Integer.toString(streak));
            streakTextView.setVisibility(VISIBLE);
        } else {
            streakTextView.setVisibility(View.GONE);
        }

        habitEvent = habit.getEvents().getHabitEvent(0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        
        // Create Map
        if (habitEvent.getLocation() != null) {
            mapFragment.getView().setVisibility(VISIBLE);
            mapFragment.getMapAsync(this);
        } else {
            mapFragment.getView().setVisibility(View.GONE);
        }

        TextView date = (TextView) findViewById(R.id.eventDate_friend);
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        date.setText(formatter.format(habitEvent.getEventDate()));
        TextView comment = (TextView) findViewById(R.id.comment_friend);
        comment.setText(habitEvent.getComment());
        ImageView image = (ImageView) findViewById(R.id.friendEventImage);
        // set image if there is one
        if (habitEvent.getBitmapString() != null) {
            image.setVisibility(VISIBLE);
            image.setImageBitmap(SerializableImage.getBitmapFromString(habitEvent.getBitmapString()));
        }
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
     * Changes and aligns all font on screen
     */
    private void changeFont() {
        Typeface ralewayRegular = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        TextView titleText = findViewById(R.id.title_friend);
        TextView reasonText = findViewById(R.id.textViewReason);
        TextView startDateText = findViewById(R.id.textViewStartDate);
        TextView freqText = findViewById(R.id.textViewFreq);
        TextView reason = findViewById(R.id.reason_friend);
        TextView startDate = findViewById(R.id.startDate_friend);
        TextView frequency = findViewById(R.id.frequency_friend);
        TextView streakText = findViewById(R.id.streakTextView);
        TextView milestoneText = findViewById(R.id.milestoneTextView);
        TextView eventDate = findViewById(R.id.eventDate_friend);
        TextView eventDateText = findViewById(R.id.textViewEventDate_friend);
        TextView comment = findViewById(R.id.comment_friend);
        TextView commentText = findViewById(R.id.textViewComment_friend);

        titleText.setTypeface(ralewayRegular);
        reasonText.setTypeface(ralewayRegular);
        startDateText.setTypeface(ralewayRegular);
        freqText.setTypeface(ralewayRegular);
        reason.setTypeface(ralewayRegular);
        startDate.setTypeface(ralewayRegular);
        frequency.setTypeface(ralewayRegular);
        streakText.setTypeface(ralewayRegular);
        milestoneText.setTypeface(ralewayRegular);
        eventDate.setTypeface(ralewayRegular);
        eventDateText.setTypeface(ralewayRegular);
        comment.setTypeface(ralewayRegular);
        commentText.setTypeface(ralewayRegular);
    }


    /**
     * Sets the map view if the user has a location attached to the most recent habit event
     * @param googleMap
     */
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
}
