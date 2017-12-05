/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Habits;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.baard.Controllers.FileController;
import com.example.baard.Entities.Day;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitList;
import com.example.baard.Entities.User;
import com.example.baard.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.zip.DataFormatException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewHabitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author bangotti, rderbysh
 */
public class CreateNewHabitFragment extends Fragment {

    private EditText titleText;
    private EditText reasonText;
    private EditText startDateText;
    private ArrayList<Day> frequency = new ArrayList<>();
    private HabitList habits;
    private HashSet<String> habitNames = new HashSet<>();
    private FileController fc;
    private User user;
    private ArrayList<ToggleButton> toggles = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public CreateNewHabitFragment() {
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateNewHabitFragment.
     */
    public static CreateNewHabitFragment newInstance() {
        CreateNewHabitFragment fragment = new CreateNewHabitFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when create habit activity is first created
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
     * Called when create habit activity is first created
     *
     * @param inflater The layout inflater
     * @param container Container for the ViewGroup
     * @param savedInstanceState Bundle of the saved State
     * @return View of the fragment activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_create_new_habit, container, false);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        String username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        fc = new FileController();
        user = fc.loadUser(getActivity().getApplicationContext(), username);
        habits = user.getHabits();

        for (int i = 0; i < habits.size(); i++) {
            habitNames.add(habits.getHabit(i).getTitle().toLowerCase());
        }

        Button createButton = myView.findViewById(R.id.create);
        titleText = myView.findViewById(R.id.title);
        reasonText = myView.findViewById(R.id.reason);
        startDateText = myView.findViewById(R.id.startDate);
        startDateText.setFocusable(false);
        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        startDateText.setText(sdf.format(calendar.getTime()));
                    }
                };

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });

        // grab all toggle buttons
        toggles.add((ToggleButton) myView.findViewById(R.id.sun));
        toggles.add((ToggleButton) myView.findViewById(R.id.mon));
        toggles.add((ToggleButton) myView.findViewById(R.id.tue));
        toggles.add((ToggleButton) myView.findViewById(R.id.wed));
        toggles.add((ToggleButton) myView.findViewById(R.id.thu));
        toggles.add((ToggleButton) myView.findViewById(R.id.fri));
        toggles.add((ToggleButton) myView.findViewById(R.id.sat));

        // set the toggle buttons for the days of the week
        setToggleButtons(myView);

        // set the function to the create button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createHabit();
            }
        });

        changeFont(myView);

        // Inflate the layout for this fragment
        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == getActivity().RESULT_OK ) {
                getActivity().onBackPressed();
            }
        }
    }

    private void changeFont(View myView) {
        Typeface ralewayRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");

        // Change font
        TextView newHabit_title = myView.findViewById(R.id.newHabit_title);
        TextView newHabit_reason = myView.findViewById(R.id.newHabit_reason);
        TextView newHabit_startDate = myView.findViewById(R.id.newHabit_startDate);
        TextView newHabit_daysOfWeek = myView.findViewById(R.id.newHabit_daysOfWeek);
        newHabit_title.setTypeface(ralewayRegular);
        newHabit_reason.setTypeface(ralewayRegular);
        newHabit_startDate.setTypeface(ralewayRegular);
        newHabit_daysOfWeek.setTypeface(ralewayRegular);
        Button createButton = myView.findViewById(R.id.create);
        createButton.setTypeface(ralewayRegular);
        titleText = myView.findViewById(R.id.title);
        titleText.setTypeface(ralewayRegular);
        reasonText = myView.findViewById(R.id.reason);
        reasonText.setTypeface(ralewayRegular);
        startDateText = myView.findViewById(R.id.startDate);
        startDateText.setTypeface(ralewayRegular);

        for (ToggleButton toggle : toggles) {
            toggle.setTypeface(ralewayRegular);
        }
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
     * Method called when save button is pressed. Creates a new Habit and adds it to the
     * user's list.
     */
    public void createHabit() {
        Boolean properEntry = true;
        String title_text = titleText.getText().toString();
        String reason = reasonText.getText().toString();
        Date convertedStartDate = convertDate(startDateText.getText().toString());

        // throw errors if the user does not input into the mandatory fields
        if (title_text.equals("")) {
            titleText.setError("Title of habit is required!");
            properEntry = false;
        } else if (habitNames.contains(title_text.toLowerCase())) {
            titleText.setError("Title of habit must be unique!");
            properEntry = false;
        }
        if (reason.equals("")) {
            reasonText.setError("Reason for habit is required!");
            properEntry = false;
        }
        if (convertedStartDate == null) {
            startDateText.setError("Start date is required!");
            properEntry = false;
        }
        if (frequency.size() < 1) {
            Toast.makeText(getActivity(), "No frequency selected", Toast.LENGTH_LONG).show();
            properEntry = false;
        }

        // if all of the values are entered try to save
        if (properEntry) {
            try {
                Habit habit = new Habit(title_text, reason, convertedStartDate, frequency);
                habits.add(habit);

                Collections.sort(habits.getArrayList());
                fc.saveUser(getActivity().getApplicationContext(), user);

                Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
                intent.putExtra("position", habits.indexOf(habit));
                startActivityForResult(intent, 2);
            } catch (DataFormatException errMsg) {
                // occurs when title or reason are above their character limits
                Toast.makeText(getActivity(), errMsg.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sets the required toggle buttons for the days of the week.
     * This thereby controls the frequency array to which habits should repeat on.
     */
    public void setToggleButtons(View myView) {
        // grab all possible enum values of Day
        final Day[] possibleValues  = Day.values();

        // iterate through all the toggle buttons to set the listener
        for (int i = 0; i < toggles.size(); i++) {
            final int finalI = i;
            toggles.get(i).setChecked(false);
            toggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // if the Day is not in the frequency already, add it
                        if (!frequency.contains(possibleValues[finalI])) {
                            frequency.add(possibleValues[finalI]);
                        }
                    } else {
                        // remove the Day from the frequency
                        frequency.remove(possibleValues[finalI]);
                    }
                }
            });
        }
    }

    /**
     * Auto-generated method for fragment
     *
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
     * Converts the date from the input of a string format to a date format.
     *
     * @param stringDate
     * @return
     */
    public Date convertDate(String stringDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        format.setLenient(false);
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
