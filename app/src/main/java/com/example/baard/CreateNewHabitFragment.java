/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import org.w3c.dom.Node;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewHabitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewHabitFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HabitList habits = new HabitList();
    ArrayList<ToggleButton> toggles = new ArrayList<>();
    //ArrayList<Day> trueToggles = new ArrayList<Day>();

    private ArrayAdapter<Habit> adapter;
    private EditText titleText;
    private EditText reasonText;
    private EditText startDateText;
    private ArrayList<Day> frequency;

    private OnFragmentInteractionListener mListener;

    public CreateNewHabitFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewHabitFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static CreateNewHabitFragment newInstance(String param1, String param2) {
        CreateNewHabitFragment fragment = new CreateNewHabitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /** Called when create habit activity is first created
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * called when CreateNewHabit activity is opened up and called again.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    public void setToggleButtons(ArrayList<Day> days) {

    }

    /**
     * Called when create habit activity is first created
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_create_new_habit, container, false);

        Button createButton = (Button) myView.findViewById(R.id.create);
        titleText = (EditText) myView.findViewById(R.id.title);
        reasonText = (EditText) myView.findViewById(R.id.reason);
        startDateText = (EditText) myView.findViewById(R.id.startDate);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean properEntry = true;

                Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
                String title_text = titleText.getText().toString();
                String reason = reasonText.getText().toString();

                String startDate = startDateText.getText().toString();
                Date convertedStartDate = convertDate(startDate);

                setToggleButtons();

                if (titleText.getText().toString().equals("")) {
                    titleText.setError("Title of habit is required!");
                    properEntry = false;
                }
                if (reasonText.getText().toString().equals("")) {
                    reasonText.setError("Reason for habit is required!");
                    properEntry = false;
                }
                if (startDateText.getText().toString().equals("")) {
                    startDateText.setError("Start date is required!");
                    properEntry = false;
                }

                if (properEntry) {

                    habits.add(new Habit(title_text, reason, convertedStartDate, frequency));
                    //System.out.println("Start Date: " + convertedStartDate);
                    // System.out.println("freq" + habits.getHabit(0).getFrequency());
                    startActivity(intent);
                }
            }
        });
        // Inflate the layout for this fragment
        return myView;
    }

    /**
     * @param uri
     */
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Sets the toggle buttons for the days of the week when the buttons are pushed on or off.
     */
    public void setToggleButtons() {
        //ArrayList<ToggleButton> toggles = new ArrayList<>();
        toggles.add((ToggleButton) getView().findViewById(R.id.sun));
        toggles.add((ToggleButton) getView().findViewById(R.id.mon));
        toggles.add((ToggleButton) getView().findViewById(R.id.tue));
        toggles.add((ToggleButton) getView().findViewById(R.id.wed));
        toggles.add((ToggleButton) getView().findViewById(R.id.thu));
        toggles.add((ToggleButton) getView().findViewById(R.id.fri));
        toggles.add((ToggleButton) getView().findViewById(R.id.sat));
        final Day[] possibleValues  = Day.values();

        for (int i = 0; i < toggles.size(); i++) {
            final int finalI = i;
            toggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // TODO The toggle is enabled
                        if (!frequency.contains(possibleValues[finalI])) {
                            frequency.add(possibleValues[finalI]);
                        }
                    } else {
                        // TODO The toggle is disabled
                        frequency.remove(possibleValues[finalI]);
                    }
                }
            });
//            //TODO set it on or off depending on arraylist state!!
//            if (frequency.contains(possibleValues[finalI])) {
//                toggles.get(i).setChecked(true);
//            }
        }
    }

    /**
     * @param context
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
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Converts the date from the input of a string format to a date format.
     *
     * @param stringDate
     * @return
     */
    public Date convertDate(String stringDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(" Please enter date ");
        }
        return date;
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
