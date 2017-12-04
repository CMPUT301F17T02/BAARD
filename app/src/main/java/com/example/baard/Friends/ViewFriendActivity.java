/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Friends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class ViewFriendActivity extends AppCompatActivity {

    private int position;
    private String friendName, friendUsername;
    private String username;
    private FileController fileController;
    private User user;

    /**
     * This create method sets the text based on habit retrieved
     * @param savedInstanceState Bundle for the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        fileController = new FileController();

        // grab the index of the item in the list
        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        friendUsername = extras.getString("friendUsername");
        friendName = extras.getString("friendName");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        //TODO: GRAB FRIEND USERNAME FROM FRIENDS MAP
        String json = sharedPrefs.getString("username", "");

        //Getting user info of friend
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());
        user = fileController.loadUser(getApplicationContext(), username);

    }

    /**
     * Load the user to display the updated habit
     */
    @Override
    public void onStart() {
        super.onStart();

        final User friend = fileController.loadUserFromServer(friendUsername);

        final List habitList = friend.getHabits().getArrayList();

        ListView listView = (ListView) findViewById(R.id.habit_scroller_listview);
        TextView title = (TextView) findViewById(R.id.friend_title);
        title.setText(friendName);

        ArrayAdapter<Habit> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (friend.getHabits().getHabit(i).getEvents().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No habit events to view", Toast.LENGTH_SHORT).show();
                } else {
                    //tell the ViewRecordActivity which list item has been selected and start it
                    Intent intent = new Intent(getApplicationContext(), ViewFriendHabitActivity.class);
                    intent.putExtra("HabitPosition", i);
                    intent.putExtra("habitName", habitList.get(i).toString());
                    intent.putExtra("user", friend.getUsername());
                    startActivity(intent);
                }
            }
        });

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
