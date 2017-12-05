/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Users can see who requested to follow them, and then accept or delete that request.
 * @see FileController
 * @author rderbysh
 * @since 1.0
 */
public class FriendRequestsFragment extends Fragment {

    private ExpandableListView friendRequestsView;
    private MyFriendsRequestAdapter adapter;
    private String username;
    private FileController fileController;
    private ArrayList<String> getFriendRequestsList = new ArrayList<>();
    private HashMap<String, Boolean> getFriendRequestsMap = new HashMap<String, Boolean>();
    private User user;
    private HashMap<String, String> userMap = new HashMap<String, String>();
    private ArrayList<String> fullFriendRequestsList = new ArrayList<String>();


    private FriendRequestsFragment.OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public FriendRequestsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FriendRequestsFragment.
     */
    public static FriendRequestsFragment newInstance() {
        FriendRequestsFragment fragment = new FriendRequestsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Sets the on item click listener such that habits in the list can be accessed by
     * the view screen.
     *
     * @param inflater The layout inflater
     * @param container Container for the ViewGroup
     * @param savedInstanceState Bundle of the saved State
     * @return View of the fragment activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        fileController = new FileController();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        friendRequestsView = view.findViewById(R.id.friendRequestsView);

        return view;
    }

    /**
     * Called when FriendRequestsFragment activity is opened up and called again.
     */
    @Override
    public void onResume() {
        super.onResume();

        user = fileController.loadUser(getActivity().getApplicationContext(), username);

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<String>> listDataChild = new HashMap<>();
        List<String> child = new ArrayList<>();
        child.add("");

        userMap = fileController.getAllUsers();
        getFriendRequestsMap = user.getReceivedRequests();
        System.out.println("getFriendRequestsMap: " + getFriendRequestsMap);

        if (!(getFriendRequestsList.size()>0)) {
            fullFriendRequestsList.addAll(getFriendRequestsMap.keySet());
            for (int i=0; i<getFriendRequestsMap.size();i++) {
                if (getFriendRequestsMap.get(fullFriendRequestsList.get(i))) {
                    getFriendRequestsList.add(fullFriendRequestsList.get(i));
                    System.out.println("getFriendRequestsList: " + getFriendRequestsList);
                }
            }
        }

        if (!getFriendRequestsMap.isEmpty()) {
            System.out.println("User's received requests: " + getFriendRequestsList);
            for (int j = 0; j < getFriendRequestsList.size(); j++) {
                String name = userMap.get(getFriendRequestsList.get(j));
                if (name != null) {
                    listDataHeader.add(name);
                    listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), child);
                }
            }
        }

        adapter = new MyFriendsRequestAdapter(this.getContext(), listDataHeader, listDataChild, getFriendRequestsList, getFriendRequestsList);

        friendRequestsView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
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
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Adapter for the expandable list view so users can either accept or delete a request.
     */
    private class MyFriendsRequestAdapter extends BaseExpandableListAdapter {

        // Referenced and copied from https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
        private final Context _context;
        private final List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private final HashMap<String, List<String>> _listDataChild;
        private final ArrayList<String> allUsersList;
        private final ArrayList<String> seenUsersList;

        /**
         * Constructor for Expandable List Adapter
         * @param context The context of the Application
         * @param listDataHeader The header names for the list (Habit names)
         * @param listChildData Child Strings (not used in this implementation) Pass in HashMap with Strings of Headers and a List<String> of just ""
         * @param allUsersList All habits of the user
         * @param seenUsersList List of Habits on the screen (Daily or All)
         */
        public MyFriendsRequestAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData, ArrayList<String> allUsersList, ArrayList<String> seenUsersList) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
            this.allUsersList = allUsersList;
            this.seenUsersList = seenUsersList;
        }


        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_friend_request, null);
            }

            convertView.findViewById(R.id.acceptFriendButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String acceptPerson = seenUsersList.get(groupPosition);
                    allUsersList.remove(acceptPerson);
                    getFriendRequestsList.remove(acceptPerson);
                    getFriendRequestsMap.put(acceptPerson, Boolean.FALSE);
                    user.getReceivedRequests().put(acceptPerson, Boolean.FALSE);

                    Boolean test = fileController.acceptFriendRequest(getContext(), user.getUsername(), acceptPerson);
                    if (test) { System.out.println("Saved to server"); }
                    else { System.out.println("Failed"); }

                    fileController.saveUser(_context, user);
                    _listDataHeader.remove(groupPosition);
                    notifyDataSetChanged();
                }
            });

            convertView.findViewById(R.id.declineFriendButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String declinedPerson = seenUsersList.get(groupPosition);
                    allUsersList.remove(declinedPerson);
                    getFriendRequestsList.remove(declinedPerson);
                    getFriendRequestsMap.put(declinedPerson, Boolean.FALSE);

                    user.getReceivedRequests().put(declinedPerson, Boolean.FALSE);
                    fileController.saveUser(_context, user);

                    _listDataHeader.remove(groupPosition);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


}