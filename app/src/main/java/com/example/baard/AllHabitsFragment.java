/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllHabitsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllHabitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @see MainActivity
 */
public class AllHabitsFragment extends Fragment {
    private ExpandableListView expandableListView;
    private String username;
    private FileController fc;

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public AllHabitsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AllHabitsFragment.
     */
    public static AllHabitsFragment newInstance() {
        AllHabitsFragment fragment = new AllHabitsFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_habits, container, false);
        fc = new FileController();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        expandableListView = view.findViewById(R.id.habitListView);

        return view;
    }

    /**
     * Called when AllHabitsFragment activity is opened up and called again.
     */
    @Override
    public void onResume() {
        super.onResume();

        User user = fc.loadUser(getActivity().getApplicationContext(), username);
        HabitList habitList = user.getHabits();

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<String>> listDataChild = new HashMap<>();
        List<String> child = new ArrayList<>();
        child.add("");
        for (int i = 0; i < habitList.size(); i++) {
            Habit h = habitList.getHabit(i);
            listDataHeader.add(h.getTitle());
            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), child);
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild, habitList, habitList);

        expandableListView.setAdapter(listAdapter);
    }

    /**
     * Auto-generated method for fragment
     * @param context The context of the application
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Auto-generated method for fragment
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

}
