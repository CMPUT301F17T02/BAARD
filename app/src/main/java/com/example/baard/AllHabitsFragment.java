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
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllHabitsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllHabitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllHabitsFragment extends Fragment {
    private ListView habitListView;
    private ArrayAdapter<Habit> adapter;
    private HabitList habitList;
    private String username;
    private User user;
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
        View view = inflater.inflate(R.layout.fragment_all_habits, container, false);
        fc = new FileController();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        habitListView = (ListView) view.findViewById(R.id.habitListView);

        // set the listener so that if you click a habit in the list, you can view it
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
                intent.putExtra("position", i);
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

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        user = fc.loadUser(getActivity().getApplicationContext(), username);
        habitList = user.getHabits();

        adapter = new ArrayAdapter<Habit>(getActivity(), R.layout.list_item, habitList.getArrayList());
        habitListView.setAdapter(adapter);

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
     * Auto-generated method for fragment
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
