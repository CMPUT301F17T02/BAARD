/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

/**
 * Created by randi on 23/11/17.
 */

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class FindFriendsFragment extends Fragment {

    private ListView findFriendsView;
    private ArrayAdapter<Habit> adapter;
    private HabitList habitList;
    private String username;
    private User user;
    private FileController fc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_friends, container, false);

        //fc = new FileController();

        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //Gson gson = new Gson();
        //String json = sharedPrefs.getString("username", "");
        //username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        // dummy data for now just to play around with things
        ArrayList<User> allUserList = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            allUserList.add(new User(Integer.toString(i), Integer.toString(i), Integer.toString(i)));
        }
        return rootView;
    }


}
