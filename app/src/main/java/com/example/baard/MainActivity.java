/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateNewHabitFragment.OnFragmentInteractionListener,
        AllHabitsFragment.OnFragmentInteractionListener, AllHabitEventsFragment.OnFragmentInteractionListener,
        CreateNewHabitEventFragment.OnFragmentInteractionListener {

    /**
     * On create method for entire activity. Sets up navigation and listener for fragments
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Functionality coming soon!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // view all habits -- front screen to view
        AllHabitsFragment allHabitsFragment = AllHabitsFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.relativelayout_for_fragment,
                allHabitsFragment,
                allHabitsFragment.getTag()
        ).commit();
    }

    /**
     * Hides navigation bar when back button is pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Sets up action bar and menu.
     * Auto-generated method by the navigation menu activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * Auto-generated method by the navigation menu activity.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When navigation menu item is selected, it compares the id to send the
     * user to a specific fragment.
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dailyHabits) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_allHabits) {
            // Send user to fragment that shows list of all their habits
            // They can view, edit, and delete a habit once they click on a habit in this list
            Toast.makeText(this, "All Habits", Toast.LENGTH_SHORT).show();
            AllHabitsFragment allHabitsFragment = AllHabitsFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    allHabitsFragment,
                    allHabitsFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_newHabit) {
            // Send user to fragment that allows them to create a new habit
            Toast.makeText(this, "Create New Habit", Toast.LENGTH_SHORT).show();
            CreateNewHabitFragment createNewHabitFragment = CreateNewHabitFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    createNewHabitFragment,
                    createNewHabitFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_allHabitEvents) {
            // Send user to fragment that shows a list of all their habit events
            // listed with most recent habit events first
            Toast.makeText(this, "Habit Event History", Toast.LENGTH_SHORT).show();
            AllHabitEventsFragment allHabitEventsFragment = AllHabitEventsFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    allHabitEventsFragment,
                    allHabitEventsFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_newHabitEvent) {
            // Send user to fragment that allows them to create a new habit event
            Toast.makeText(this, "Create New Habit Event", Toast.LENGTH_SHORT).show();
            CreateNewHabitEventFragment createNewHabitEventFragment = CreateNewHabitEventFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    createNewHabitEventFragment,
                    createNewHabitEventFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_viewMap) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_viewFriends) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            // End this session and take users back to the login screen
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            sharedPrefs.edit().remove("username").commit();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Auto-generated method by the navigation menu activity.
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
