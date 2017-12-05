/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.baard.Controllers.FileController;
import com.example.baard.Controllers.TypefaceSpan;
import com.example.baard.Friends.ExploreFriends;
import com.example.baard.HabitEvents.AllHabitEventsFragment;
import com.example.baard.HabitEvents.CreateNewHabitEventFragment;
import com.example.baard.Habits.AllHabitsFragment;
import com.example.baard.Habits.CreateNewHabitFragment;
import com.example.baard.Habits.DailyHabitsFragment;

import java.util.Stack;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Implements the MainActivity for the app that is reached after logging in
 * @see LoginActivity
 * @see NavigationView
 * @see CreateNewHabitFragment
 * @see CreateNewHabitEventFragment
 * @see AllHabitsFragment
 * @see AllHabitEventsFragment
 * @see DailyHabitsFragment
 * @see HelpFragment
 * @see ViewMapActivity
 * @see ExploreFriends
 * @see SettingsActivity
 * @author bangotti, anarten
 * @since 1.0
 * @version 2.2
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateNewHabitFragment.OnFragmentInteractionListener,
        AllHabitsFragment.OnFragmentInteractionListener, AllHabitEventsFragment.OnFragmentInteractionListener,
        CreateNewHabitEventFragment.OnFragmentInteractionListener, DailyHabitsFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener {

    private Stack<String> headerStack = new Stack<>();
    private String nextHeader;

    /**
     * On create method for entire activity. Sets up navigation and listener for fragments
     *
     * @param savedInstanceState Bundle for the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // view all habits -- front screen to view
        Fragment fragment = new DailyHabitsFragment();
        nextHeader = getResources().getString(R.string.daily_habits);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.relativelayout_for_fragment, fragment, fragment.getTag())
                .addToBackStack(null)
                .commit();
        navigationView.setCheckedItem(R.id.nav_dailyHabits);
        setActionBarTitle(getString(R.string.daily_habits));
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
     * Hides navigation bar when back button is pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (headerStack.empty()) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            sharedPrefs.edit().remove("username").apply();
            finish();
        } else {
            String name=headerStack.pop();
            System.out.println(name);
            setActionBarTitle(name);
            super.onBackPressed();
        }
    }

    /**
     * Sets up action bar and menu.
     * Auto-generated method by the navigation menu activity.
     *
     * @param menu the menu pop up
     * @return boolean true for success
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
     * @param item the item in the options menu seleted
     * @return boolean true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    /**
     * When navigation menu item is selected, it compares the id to send the
     * user to a specific fragment.
     *
     * @param item The item selected in the navigation menu
     * @return boolean true if successful
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        headerStack.push(nextHeader);
        Fragment fragment = null;
        FileController fileController = new FileController();
        // Send user to selected fragment
        if (id == R.id.nav_dailyHabits) {
            fragment = new DailyHabitsFragment();
            nextHeader = getResources().getString(R.string.daily_habits);
        }
        else if (id == R.id.nav_allHabits) {
            fragment = new AllHabitsFragment();
            nextHeader = getResources().getString(R.string.all_habits);
        } else if (id == R.id.nav_newHabit) {
            fragment = new CreateNewHabitFragment();
            nextHeader = getResources().getString(R.string.create_habit);
        } else if (id == R.id.nav_allHabitEvents) {
            fragment = new AllHabitEventsFragment();
            nextHeader = getResources().getString(R.string.habit_history);
        } else if (id == R.id.nav_newHabitEvent) {
            fragment = new CreateNewHabitEventFragment();
            nextHeader = getResources().getString(R.string.create_event);
        } else if (id == R.id.nav_viewMap) {
            if (fileController.isNetworkAvailable(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this, ViewMapActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_viewFriends) {
            if (fileController.isNetworkAvailable(getApplicationContext())) {
                Toast.makeText(this, R.string.title_activity_explore_friends, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ExploreFriends.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_help) {
            fragment = new HelpFragment();
            nextHeader = getResources().getString(R.string.help);
        } else if (id == R.id.nav_logout) {
            // End this session and take users back to the login screen
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
            sharedPrefsEditor.clear();
            sharedPrefsEditor.commit();
            finish();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.relativelayout_for_fragment, fragment, fragment.getTag())
                    .addToBackStack(null)
                    .commit();
            setActionBarTitle(nextHeader);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Auto-generated method by the navigation menu activity.
     *
     * @param uri Uri of the fragment
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
