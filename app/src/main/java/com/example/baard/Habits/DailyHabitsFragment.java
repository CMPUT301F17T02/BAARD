/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Habits;

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

import com.example.baard.Controllers.ExpandableListAdapter;
import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.Day;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitList;
import com.example.baard.Entities.User;
import com.example.baard.MainActivity;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Shows the habits whose frequency is on this current day. The user can then view, edit or
 * delete this habit.
 * @see MainActivity
 * @author anarten
 * @note based on setup from all habits fragment
 */
public class DailyHabitsFragment extends Fragment {
    private FileController fc;
    private String username;
    private ExpandableListView expandableListView;
    private SharedPreferences sharedPrefs;
    private Gson gson;

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public DailyHabitsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DailyHabitsFragment.
     */
    public static DailyHabitsFragment newInstance() {
        DailyHabitsFragment fragment = new DailyHabitsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_habits, container, false);
        fc = new FileController();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        expandableListView = view.findViewById(R.id.dailyHabitsListView);

        return view;
    }

    /**
     * Called when DailyHabitsFragment fragment is opened up and called again.
     */
    @Override
    public void onResume() {
        super.onResume();

        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());
        if (username != null) {
            User user = fc.loadUser(getActivity().getApplicationContext(), username);
            if (user == null) {
                getActivity().finish();
                return;
            }
            HabitList habitList = user.getHabits();

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat sDF = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String today = sDF.format(date.getTime()).toUpperCase();
            Day day = Day.valueOf(today);

            List<String> listDataHeader = new ArrayList<>();
            HashMap<String, List<String>> listDataChild = new HashMap<>();
            HabitList dailyHabitList = new HabitList();
            List<String> child = new ArrayList<>();
            child.add("");
            for (int i = 0; i < habitList.size(); i++) {
                Habit h = habitList.getHabit(i);
                ArrayList<Day> freq = h.getFrequency();
                if (freq.contains(day)) {
                    dailyHabitList.add(h);
                    listDataHeader.add(h.getTitle());
                    listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), child);
                }
            }

            ExpandableListAdapter listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild, habitList, dailyHabitList);

            expandableListView.setAdapter(listAdapter);
        } else {
            getActivity().finish();
        }
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
