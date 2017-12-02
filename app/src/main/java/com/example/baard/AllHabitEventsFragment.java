/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

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
//    private ArrayAdapter<HabitEvent> adapter;
    private List<Habit> habitList;
    private List<HabitEvent> habitEventList = new ArrayList<HabitEvent>();
    private final FileController fileController = new FileController();
    Habit noneHabit;
    Spinner habitSpinner;
    EditText commentFilter;

    private OnFragmentInteractionListener mListener;

    public AllHabitEventsFragment() {
        // Required empty public constructor
    }

    private String getUsername(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        return gson.fromJson(json, new TypeToken<String>() {}.getType());
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllHabitEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllHabitEventsFragment newInstance() {
        AllHabitEventsFragment fragment = new AllHabitEventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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

        habitList = user.getHabits().getArrayList();
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

//        adapter = new ArrayAdapter<HabitEvent>(getActivity(), R.layout.list_item, habitEventList);

        sendHabitEventsToSharedPreferences();

//        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //tell the ViewRecordActivity which list item has been selected and start it
//                Intent intent = new Intent(getActivity(), ViewHabitEventActivity.class);
//                //TODO: PASS HABITEVENT TO VIEWHABITEVENTACTIVITY SOMEHOW
//                intent.putExtra("habitEventDate",habitEventList.get(i).getEventDate().toString());
//                habitEventList.get(i).getHabit().sendToSharedPreferences(getActivity().getApplicationContext());
//                startActivity(intent);
//            }
//        });

        filterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                filterHabitEvents();
            }
        });

//        habitEventListView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void sendHabitEventsToSharedPreferences(){
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(this.getContext());
        SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habitEventList);
        sharedPrefsEditor.putString("filteredHabitEvents", json);
        sharedPrefsEditor.commit();
    }

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

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild, habitEventList);

        expandableEventListView.setAdapter(listAdapter);
//        adapter.notifyDataSetChanged();
        sendHabitEventsToSharedPreferences();
    }

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
     * this onResume has been overrided to update the data set of all HabitEvents being viewed, in case one has been
     * deleted since the user was on this screen last.
     */
    @Override
    public void onResume() {
        super.onResume();

        filterHabitEvents();
//        createHabitEventList();
//
//        List<String> listDataHeader = new ArrayList<>();
//        HashMap<String, List<String>> listDataChild = new HashMap<>();
//        List<String> child = new ArrayList<>();
//        child.add("");
//        for (int i = 0; i < habitEventList.size(); i++) {
//            HabitEvent event = habitEventList.get(i);
//            listDataHeader.add(event.toString());
//            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), child);
//        }
//
//        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild, habitEventList);
//
//        expandableEventListView.setAdapter(listAdapter);

//        adapter = new ArrayAdapter<HabitEvent>(getActivity(), R.layout.list_item, habitEventList);
//        expandableEventListView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
