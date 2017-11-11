/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewHabitEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewHabitEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewHabitEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int PICK_IMAGE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateNewHabitEventFragment() {
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
    public static CreateNewHabitEventFragment newInstance(String param1, String param2) {
        CreateNewHabitEventFragment fragment = new CreateNewHabitEventFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_new_habit_event, container, false);

        // TODO: GET ACTUAL LIST OF HABITS FROM FILE/SERVER
        String [] habits = {"Swimming","Eating","Studying"};
        Spinner spinner = (Spinner) v.findViewById(R.id.habitSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, habits);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        Button imageButton = (Button) v.findViewById(R.id.attachImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                onSelectImageButtonPress(view);
            }
        });
        Button createButton = (Button) v.findViewById(R.id.saveButton);
        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                createHabitEvent();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void createHabitEvent(){
        //validate data fields and save the record BOI
        //make sure date string is a valid format
        Date date = null;
        boolean isValidHabitEvent = true;
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        EditText dateEditText = (EditText) getActivity().findViewById(R.id.HabitEventDateEditText);
        try {
            date = sourceFormat.parse(dateEditText.getText().toString());
        }catch(Exception e){
            //invalid date format
            isValidHabitEvent = false;
        }
        //TODO: make sure there are no HabitEvents on the given date

        if (isValidHabitEvent) {
            //Habit habit = new Habit();
            EditText comment = (EditText) getActivity().findViewById(R.id.commentEditText);
            //HabitEvent habitEvent = new HabitEvent(habit, date, comment);
            //TODO: SAVE HABIT EVENT IN HABIT 
        }
    }

    public void onSelectImageButtonPress(View view){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            //TODO: action

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            TextView textView = (TextView) getActivity().findViewById(R.id.filenameTextView);
            textView.setText(filePath);
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
            //imageView.setImageBitmap(yourSelectedImage);
            imageView.setImageURI(selectedImage);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();
        //get eerything ready
        // go super saiyan 3
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
