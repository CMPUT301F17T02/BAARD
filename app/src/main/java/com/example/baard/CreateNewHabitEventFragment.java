/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.zip.DataFormatException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewHabitEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewHabitEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author amckerna
 * @version 1.0
 * This fragment is used for when creating a new HabitEvent.
 */
public class CreateNewHabitEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int PICK_IMAGE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final FileController fileController = new FileController();

    // TODO: Rename and change types of parameters
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String mParam1;
    private String mParam2;
    private Habit habit = null;
    private HabitList habits;
    private String imageFilePath;
    private User user = null;

    private OnFragmentInteractionListener mListener;

    public CreateNewHabitEventFragment() {
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

    /**
     * onCreateView - called when view is constructed, sets listeners and needed variables.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_new_habit_event, container, false);

        // get list of habits from user
        //FileController fileController = new FileController();
        user = fileController.loadUser(getActivity().getApplicationContext(), getUsername());
        habits = user.getHabits();
        //String [] habits = {"Swimming","Eating","Studying"};
        Spinner spinner = (Spinner) v.findViewById(R.id.habitSpinner);
        ArrayAdapter<Habit> adapter = new ArrayAdapter<Habit>(this.getActivity(), android.R.layout.simple_spinner_item, habits.getArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        Button locationButton = (Button) v.findViewById(R.id.addLocationButton);
        locationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(), "COMING SOON!", Toast.LENGTH_SHORT).show();
            }
        });
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //set the selected Habit to the habit
                // habit = ThatOneHabit
                habit = habits.getHabit(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // can this even happen? Do nothing for now.
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

    /**
     * Method called when save button is pressed. Creates a new HabitEvent and adds it to the
     * HabitEventList of the Habit selected by the Spinner.
     */
    public void createHabitEvent(){
        //validate data fields and save the record BOI
        //make sure date string is a valid format
        HabitEvent habitEvent = null;
        Date date = null;
        String comment = "";
        boolean isValidHabitEvent = true;
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        EditText dateEditText = (EditText) getActivity().findViewById(R.id.HabitEventDateEditText);
        EditText commentEditText = (EditText) getActivity().findViewById(R.id.commentEditText);
        try {
            date = sourceFormat.parse(dateEditText.getText().toString());
            comment = commentEditText.getText().toString();
            habitEvent = new HabitEvent(habit, date, comment);
        }catch(DataFormatException d){
            commentEditText.setError("Comment is too long (20 char max).");
            isValidHabitEvent = false;
        }
        catch (IllegalArgumentException i){
            dateEditText.setError("Date is before habit start date. (" + habit.getStartDate().toString() + ")");
            isValidHabitEvent = false;
        }
        catch(Exception e){
            //invalid date format
            dateEditText.setError("Invalid date entry:");
            isValidHabitEvent = false;
        }
        //TODO: make sure there are no HabitEvents on the given date

        if (isValidHabitEvent) {
            if (imageFilePath != null){
                habitEvent.setImageFilePath(imageFilePath);
            }
            habit.getEvents().add(habitEvent);
            // sort on insert
            Collections.sort(habit.getEvents().getArrayList());
            fileController.saveUser(getActivity().getApplicationContext(), user);
            habit.sendToSharedPreferences(getActivity().getApplicationContext());
            // go to view habitevent activity
            Intent intent = new Intent(getActivity(), ViewHabitEventActivity.class);
            intent.putExtra("habitEventDate",habitEvent.getEventDate().toString());
            startActivity(intent);
        }
    }

    public int checkReadPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == -1){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        return permissionCheck;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                } else {
                    //permission denied
                }
                return;
            }
        }
    }

    /**
     * Method called when the select image button is pressed. Lets the user select an image to be added to the
     * habit event. Calls startActivityForResult to handle their selection.
     * @param view supplied when button is pressed
     */
    public void onSelectImageButtonPress(View view){
        //if (checkReadPermission() == -1){
        //    return;
       ///}
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    /**
     * Handles the user selecting an image from their photos. When an image is selected, the imageURI
     * variable is set to the selected image, and a preview of the image is displayed on screen.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            TextView textView = (TextView) getActivity().findViewById(R.id.filenameTextView);
            imageFilePath = filePath;
            textView.setText(filePath);
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
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
