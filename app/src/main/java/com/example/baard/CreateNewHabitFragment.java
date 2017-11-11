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
import android.widget.EditText;
import android.widget.ListView;

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

    private ArrayList<Habit> habits = new ArrayList<Habit>();
    private ArrayAdapter<Habit> adapter;
    private EditText titleText;
    private EditText reasonText;
    private EditText startDateText;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

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
                Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
                String title_text = titleText.getText().toString();
                String reason = reasonText.getText().toString();

                String startDate = startDateText.getText().toString();
                Date convertedStartDate = convertDate(startDate);

                System.out.println("title: " + title_text);
                System.out.println("reason: " + reason);
                System.out.println("start Date: " + startDate);
                System.out.println("Data start date: " + convertedStartDate);

                // Add habit to habit list
                //habits.add(new Habit(title_text, reason, convertedStartDate, ));
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


//    @Override
//    public void onClick(View v) {
//        //setResult(RESULT_OK);
//        String text = bodyText.getText().toString();
//        // Add new habit
//        //habits.add(new Habit());
//        adapter.notifyDataSetInvalidated();
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Date convertDate(String stringDate) {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
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
