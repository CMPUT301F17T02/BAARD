/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.net.Uri;
import android.os.Bundle;
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

import com.example.baard.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateNewHabitFragment.OnFragmentInteractionListener,
        AllHabitsFragment.OnFragmentInteractionListener, AllHabitEventsFragment.OnFragmentInteractionListener,
        CreateNewHabitEventFragment.OnFragmentInteractionListener {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dailyHabits) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_allHabits) {
            Toast.makeText(this, "All Habits", Toast.LENGTH_SHORT).show();
            AllHabitsFragment allHabitsFragment = AllHabitsFragment.newInstance("test", "test2");
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    allHabitsFragment,
                    allHabitsFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_newHabit) {
            Toast.makeText(this, "Create New Habit", Toast.LENGTH_SHORT).show();
            CreateNewHabitFragment createNewHabitFragment = CreateNewHabitFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    createNewHabitFragment,
                    createNewHabitFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_allHabitEvents) {
            Toast.makeText(this, "All Habit Events", Toast.LENGTH_SHORT).show();
            AllHabitEventsFragment allHabitEventsFragment = AllHabitEventsFragment.newInstance("test", "test2");
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    allHabitEventsFragment,
                    allHabitEventsFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_newHabitEvent) {
            Toast.makeText(this, "Create New Habit Event", Toast.LENGTH_SHORT).show();
            CreateNewHabitEventFragment createNewHabitEventFragment = CreateNewHabitEventFragment.newInstance("test", "test2");
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
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
