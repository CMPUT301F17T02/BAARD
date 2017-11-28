/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

/**
 * Created by randi on 23/11/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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
        findFriendsView = (ListView) rootView.findViewById(R.id.findFriendsView);


        //fc = new FileController();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());
        ArrayList<String> data = new ArrayList<>();

        // dummy data for now just to play around with things
        ArrayList<User> allUserList = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            allUserList.add(new User(Integer.toString(i), Integer.toString(i), Integer.toString(i)));
        }

        findFriendsView.setAdapter(new MyFriendsListAdapter(this.getContext(), R.layout.friend_list_item, allUserList));

        return rootView;
    }

    private class MyFriendsListAdapter extends ArrayAdapter<User> {
        private int layout;
        public MyFriendsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<User> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.addFriend);
                viewHolder.button = (Button) convertView.findViewById(R.id.addFriendButton);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.title.setText((CharSequence) getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
        Button button;
    }

}
