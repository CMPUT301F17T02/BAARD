/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.HabitEvents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.example.baard.Controllers.ExpandableListAdapter;
import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.Day;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitEvent;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllHabitEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllHabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author bangotti and amckerna
 * @version 1.0
 * This fragment displays all of the HabitEvents from every Habit in the order in which they were
 * performed.
 */
public class AllHabitEventsFragment extends Fragment {

    private ExpandableListView expandableEventListView;
    private List<HabitEvent> habitEventList = new ArrayList<HabitEvent>();
    private final FileController fileController = new FileController();
    private Habit noneHabit;
    private Spinner habitSpinner;
    private EditText commentFilter;

    private OnFragmentInteractionListener mListener;

    public AllHabitEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Get the username of the logged in user from SharedPreferences
     * @return string representing username
     */
    private String getUsername(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        return gson.fromJson(json, new TypeToken<String>() {}.getType());
    }

    /**
     * Creates the view when the fragment is started. Sets the onItemClickListener for each habitevent displayed.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_habit_events, container, false);

        User user = fileController.loadUser(getActivity().getApplicationContext(), getUsername());
        createHabitEventList();

        List<Habit> habitList = user.getHabits().getArrayList();
        try {
            noneHabit = new Habit("None", "", new Date(), new ArrayList<Day>());
            habitList.add(0,noneHabit);
        }catch(Exception e){
            //unexpected behaviour
        }
        habitSpinner = (Spinner) view.findViewById(R.id.habitFilterSpinner);


        ArrayAdapter<Habit> habitAdapter = new ArrayAdapter<Habit>(this.getActivity(), android.R.layout.simple_spinner_item, habitList);

        habitSpinner.setAdapter(habitAdapter);

        habitAdapter.notifyDataSetChanged();
        commentFilter = (EditText) view.findViewById(R.id.commentFilterEditText);

        Button filterButton = (Button) view.findViewById(R.id.filterButton);
        commentFilter.setText("");

        expandableEventListView = (ExpandableListView) view.findViewById(R.id.habitEventListView);

        sendHabitEventsToSharedPreferences();

        filterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                filterHabitEvents();
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Send the currently displayed list of Habit Events to SharedPreferences. This is used by the map
     * to filter the events seen on the map.
     */
    public void sendHabitEventsToSharedPreferences(){
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(this.getContext());
        SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habitEventList);
        sharedPrefsEditor.putString("filteredHabitEvents", json);
        sharedPrefsEditor.commit();
    }

    /**
     * Filter and display Habit Events based on the user's given parameters.
     */
    public void filterHabitEvents(){
        createHabitEventList();
        Habit selected = (Habit) habitSpinner.getSelectedItem();
        Iterator<HabitEvent> iter = habitEventList.iterator();
        while(iter.hasNext()){
            HabitEvent next = iter.next();
            if(!selected.getTitle().equals(noneHabit.getTitle()) && !next.getHabit().getTitle().equals(selected.getTitle())){
                iter.remove();
                continue;
            }
            if (!next.getComment().contains(commentFilter.getText().toString())){
                iter.remove();
            }
        }

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<String>> listDataChild = new HashMap<>();
        List<String> child = new ArrayList<>();
        child.add("");
        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent event = habitEventList.get(i);
            listDataHeader.add(event.toString());
            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), child);
        }

        ExpandableEventListAdapter listAdapter = new ExpandableEventListAdapter(this.getContext(), listDataHeader, listDataChild, habitEventList);

        expandableEventListView.setAdapter(listAdapter);
        sendHabitEventsToSharedPreferences();
    }

    /**
     * Set the habitEventList to all Habit Events in all of the users Habits.
     */
    public void createHabitEventList(){
        User user = fileController.loadUser(getActivity().getApplicationContext(), getUsername());
        habitEventList.clear();
        for (Habit habit: user.getHabits().getArrayList()) {
            for(HabitEvent habitEvent: habit.getEvents().getArrayList()){
                habitEvent.setHabit(habit);
                habitEventList.add(habitEvent);
            }
        }
        Collections.sort(habitEventList);
    }

    /**
     * This onResume has been overrided to update the data set of all HabitEvents being viewed, in case one has been
     * deleted since the user was on this screen last.
     */
    @Override
    public void onResume() {
        super.onResume();

        filterHabitEvents();
    }

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

    private class ExpandableEventListAdapter extends ExpandableListAdapter {

        private List<HabitEvent> habitEventList = null;

        public ExpandableEventListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, List<HabitEvent> habitEventList) {
            super(context, listDataHeader, listChildData, null, null);
            this.habitEventList = habitEventList;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_event_expandable, null);
            }

            convertView.findViewById(R.id.viewButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HabitEvent event = habitEventList.get(groupPosition);
                    int index = habitEventList.indexOf(event);
                    Intent intent = new Intent(_context, ViewHabitEventActivity.class);
                    intent.putExtra("habitEventDate", habitEventList.get(index).getEventDate().toString());
                    habitEventList.get(index).getHabit().sendToSharedPreferences(_context);
                    _context.startActivity(intent);
                }
            });

            convertView.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HabitEvent event = habitEventList.get(groupPosition);
                    Intent intent = new Intent(_context, EditHabitEventActivity.class);
                    intent.putExtra("habitEventDate", event.getEventDate().toString());
                    event.getHabit().sendToSharedPreferences(_context);
                    _context.startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
