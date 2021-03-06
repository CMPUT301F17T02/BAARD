/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Friends;

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

import com.example.baard.Controllers.ElasticSearchController;
import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.User;
import com.example.baard.Entities.UserList;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Finds all users in the database and displays them for the user to choose who to follow
 * @see ElasticSearchController
 * @see FileController
 * @author rderbysh
 * @since 1.0
 */
public class FindFriendsFragment extends Fragment {

    private ListView findFriendsView;
    private MyFriendsListAdapter adapter;
    private String username;
    private User user;
    private FileController fileController;
    private ElasticSearchController.GetAllUsersTask getAllUsersTask = new ElasticSearchController.GetAllUsersTask();
    private UserList allUsers = new UserList();
    private HashMap<String, Boolean> myFriends;
    private HashMap<String, String> userMap = new HashMap<String, String>();

    private ArrayList<String> acceptedFriendsList, pendingFriendsList;

    /**
     * Sets up the find friends view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_friends, container, false);

        getAllUsersTask.execute();

        findFriendsView = (ListView) rootView.findViewById(R.id.findFriendsView);

        fileController = new FileController();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        return rootView;
    }

    /**
     * Called when FindFriendsFragment activity is opened up and called again.
     */
    @Override
    public void onResume() {
        super.onResume();

        user = fileController.loadUser(getActivity().getApplicationContext(), username);

        myFriends = user.getFriends();

        try {
            allUsers = getAllUsersTask.get();

            for (User aUser : allUsers.getArrayList()) {
                userMap.put(aUser.getUsername(), aUser.getName());
                if (user.getUsername().equals(aUser.getUsername())) {
                    allUsers.delete(aUser);
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Collections.sort(allUsers.getArrayList());

        adapter = new MyFriendsListAdapter(this.getContext(), R.layout.friend_list_item, allUsers);
        findFriendsView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    /**
     * List adapter for displaying the changing buttons depending on the state on which a user
     * is following another user. (Follow, Pending, Following)
     */
    private class MyFriendsListAdapter extends ArrayAdapter<User> {
        private int layout;
        public MyFriendsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull UserList objects) {
            super(context, resource, objects.getArrayList());
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                
                acceptedFriendsList = getKeysByValue(myFriends, Boolean.TRUE);
                pendingFriendsList = getKeysByValue(myFriends, Boolean.FALSE);

                viewHolder.title = (TextView) convertView.findViewById(R.id.addFriend);
                viewHolder.button = (Button) convertView.findViewById(R.id.addFriendButton);

                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.button.getText() == "FOLLOWING") {
                            notifyDataSetChanged();
                        } else if (viewHolder.button.getText() == "PENDING") {
                            notifyDataSetChanged();
                        }
                        else {
                            viewHolder.button.setText("PENDING");
                            User friend = getItem(position);
                            System.out.println("Got friend: " + friend);
                            Boolean test = fileController.sendFriendRequest(getContext(), username, friend.getUsername());
                            if (test) { System.out.println("True: Sent to server"); }
                            user = fileController.loadUser(getContext(), username);
                            notifyDataSetChanged();
                        }
                    }
                });
                convertView.setTag(viewHolder);
            }
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position).getName());

            if (!acceptedFriendsList.isEmpty()) {
                for (int i = 0; i < acceptedFriendsList.size(); i++) {
                    String name = userMap.get(acceptedFriendsList.get(i));
                    if (name == mainViewHolder.title.getText()) {
                        mainViewHolder.button.setText("FOLLOWING");
                        updateView(position);
                    }
                }
            }
            if (!pendingFriendsList.isEmpty()) {
                for (int j = 0; j < pendingFriendsList.size(); j++) {
                    String name = userMap.get(pendingFriendsList.get(j));
                    if (name == mainViewHolder.title.getText()) {
                        mainViewHolder.button.setText("PENDING");
                        updateView(position);
                    }
                }
            }

            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
        Button button;
    }

    private void updateView(int index){
        View v = findFriendsView.getChildAt(index -
                findFriendsView.getFirstVisiblePosition());

        if(v == null)
            return;

        Button followingButton = (Button) v.findViewById(R.id.addFriendButton);
        followingButton.setText("FOLLOWING");
    }

    public static <T, V> ArrayList<T> getKeysByValue(Map<T, V> map, V value) {
        ArrayList<T> keys = new ArrayList<>();
        for (Map.Entry<T, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

}
