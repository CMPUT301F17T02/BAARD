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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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

    private ListView habitEventListView;
    private ArrayAdapter<HabitEvent> adapter;
    private List<HabitEvent> habitEventList = new ArrayList<HabitEvent>();;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final FileController fileController = new FileController();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllHabitEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllHabitEventsFragment newInstance(String param1, String param2) {
        AllHabitEventsFragment fragment = new AllHabitEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        for (Habit habit: user.getHabits().getArrayList()) {
            for(HabitEvent habitEvent: habit.getEvents().getArrayList()){
                habitEvent.setHabit(habit);
                habitEventList.add(habitEvent);
            }
        }
        Collections.sort(habitEventList);


        habitEventListView = (ListView) view.findViewById(R.id.habitEventListView);

        adapter = new ArrayAdapter<HabitEvent>(getActivity(), R.layout.list_item, habitEventList);

        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //tell the ViewRecordActivity which list item has been selected and start it
                Intent intent = new Intent(getActivity(), ViewHabitEventActivity.class);
                //TODO: PASS HABITEVENT TO VIEWHABITEVENTACTIVITY SOMEHOW
                intent.putExtra("habitEventDate",habitEventList.get(i).getEventDate().toString());
                habitEventList.get(i).getHabit().sendToSharedPreferences(getActivity().getApplicationContext());
                startActivity(intent);
            }
        });

        habitEventListView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        habitEventList.clear();
        User user = fileController.loadUser(getActivity().getApplicationContext(), getUsername());
        for (Habit habit: user.getHabits().getArrayList()) {
            for(HabitEvent habitEvent: habit.getEvents().getArrayList()){
                habitEvent.setHabit(habit);
                habitEventList.add(habitEvent);
            }
        }
        Collections.sort(habitEventList);
        adapter = new ArrayAdapter<HabitEvent>(getActivity(), R.layout.list_item, habitEventList);
        habitEventListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
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
