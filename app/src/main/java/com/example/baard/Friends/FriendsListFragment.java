/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Friends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by randi on 23/11/17.
 */


/** EVERY TIME I LOAD A FRIEND, IF THE FRIEND IS NULL, REMOVE FROM MAP **/

public class FriendsListFragment extends Fragment {

    private ListView friendListView;
    private ArrayAdapter<String> adapter;
    private String username;
    private FileController fileController;
    // List of usernames of your friends
    private ArrayList<String> friendsList = new ArrayList<>();
//    private ArrayList<String> friendsNamesList = new ArrayList<>();;
    private User user;
    // If you have friends. True if friend, False if pending
    private HashMap<String, Boolean> myFriendsMap = new HashMap<String, Boolean>();
    // Hashmap <username, name>
    private HashMap<String, String> userMap = new HashMap<String, String>();
    // If request to be friends, return true.
    private HashMap<String, Boolean> requestedFriendsMap = new HashMap<>();


    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public FriendsListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AllHabitsFragment.
     */
    public static FriendsListFragment newInstance() {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Starts on create method, could take and store arguments.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    /**
     * Sets the on item click listener such that habits in the list can be accessed by
     * the view screen.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_friends, container, false);
        fileController = new FileController();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        friendListView = (ListView) view.findViewById(R.id.friendListView);

        // set the listener so that if you click a user in the list, you can view it
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                User friend = fileController.loadUserFromServer(friendsList.get(i));

                // Make the intent go to seeing the friend's habits and most recent habit event.
                Intent intent = new Intent(getActivity(), ViewFriendActivity.class);
                intent.putExtra("position", i);
                intent.putExtra("friendUsername", friendsList.get(i));
                intent.putExtra("friendName", friend.getName());

                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Called when AllHabitsFragment activity is opened up and called again.
     */
    @Override
    public void onResume() {
        super.onResume();

        user = fileController.loadUser(getActivity().getApplicationContext(), username);

        // Get all users in hashmap
        userMap = user.getAllUsers();
        // True if friend, False if pending.
        myFriendsMap = user.getFriends();
        // Get all friends' usernames
        friendsList = getKeysByValue(myFriendsMap, Boolean.TRUE);

        // Make a clone of the list, and edit the clone list
        ArrayList<String> iterationList = (ArrayList<String>) friendsList.clone();
        for (String name : iterationList) {
            User friend = fileController.loadUserFromServer(name);

//            userMap.put(friend.getUsername(), friend.getName());

            if (friend == null) {
                myFriendsMap.put(name, false);
                friendsList.remove(name);
            }

        }

        //Get the names of all of your friends
        List<String> usernamesList = new ArrayList<String>(userMap.values());

        System.out.println("List of names: " + friendsList);
        ArrayList<String> friendsNamesList = new ArrayList<String>();
        for (String iter: friendsList) {
            if (userMap.containsKey(iter)) {
                friendsNamesList.add(userMap.get(iter));
            }
        }

        user.setFriends(myFriendsMap);
        fileController.saveUser(getContext(), user);

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, friendsNamesList);
        friendListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }


    /**
     * Auto-generated method for fragment
     *
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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