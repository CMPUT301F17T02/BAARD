/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.zip.DataFormatException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewHabitEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * @author amckerna
 * @version 1.0
 * This fragment is used for when creating a new HabitEvent.
 */
public class CreateNewHabitEventFragment extends Fragment {
    private static final int PICK_IMAGE = 1;
    private final FileController fileController = new FileController();

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Habit habit = null;
    private HabitList habits;
    private Bitmap image;
    private User user = null;
    private DateFormat sourceFormat;
    private EditText dateEditText;
    private SharedPreferences sharedPrefs;
    private Gson gson;
    private LatLng locationPosition;
//    private GoogleMap map;
//    private MapView mapView;

    private OnFragmentInteractionListener mListener;

    public CreateNewHabitEventFragment() {
        // Required empty public constructor
    }

    /**
     * returns the username of the user stored in SharedPreferences
     * @return username
     */
    private String getUsername(){
        String json = sharedPrefs.getString("username", "");
        return gson.fromJson(json, new TypeToken<String>() {}.getType());
    }

    private void clearLocation(){
        SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
        sharedPrefsEditor.remove("locationPosition");
        sharedPrefsEditor.commit();
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

        // initialize shared preferences and clear location if it exists
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        gson = new Gson();
        clearLocation();

        // get list of habits from user
        user = fileController.loadUser(getActivity().getApplicationContext(), getUsername());
        try {
            habits = user.getHabits();
        } catch(Exception e) {
            Toast.makeText(getActivity(), "Unknown error. Please try again.", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().onBackPressed();
        }

        Spinner spinner = (Spinner) v.findViewById(R.id.habitSpinner);
        ArrayAdapter<Habit> adapter = new ArrayAdapter<Habit>(this.getActivity(), android.R.layout.simple_spinner_item, habits.getArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        Button locationButton = (Button) v.findViewById(R.id.addLocationButton);
        locationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AddLocationActivity.class);
                if (locationPosition != null) {
                    intent.putExtra("myLocation", locationPosition);
                }
                startActivity(intent);
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

        sourceFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateEditText = (EditText) v.findViewById(R.id.HabitEventDateEditText);
        dateEditText.setFocusable(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateEditText.setText(sourceFormat.format(calendar.getTime()));
                    }
                };

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                if (habit.getStartDate().before(new Date())) {
                    d.getDatePicker().setMaxDate((new Date()).getTime());
                    d.getDatePicker().setMinDate(habit.getStartDate().getTime());
                    d.show();
                } else {
                    Toast.makeText(getActivity(), "Habit's start date is in the future, please choose another habit", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        String json = sharedPrefs.getString("locationPosition", "");
        locationPosition = gson.fromJson(json, new TypeToken<LatLng>() {}.getType());
        RadioButton radioButton = (RadioButton) getActivity().findViewById(R.id.radioButton);
        if (locationPosition != null) {
            radioButton.setChecked(true);
            radioButton.setText(R.string.yesLocation);
//            mapView.setVisibility(View.VISIBLE);
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(locationPosition, 15f));
//            map.addMarker(new MarkerOptions().position(locationPosition));

        } else {
            radioButton.setChecked(false);
            radioButton.setText(R.string.noLocation);
//            mapView.setVisibility(View.INVISIBLE);
        }
    }

    public void onStart() {
        super.onStart();
        EditText commentEditText = (EditText) getActivity().findViewById(R.id.commentEditText);
        try {
            Date date = sourceFormat.parse(dateEditText.getText().toString());
            HabitEvent habitEvent = new HabitEvent(habit, date, commentEditText.getText().toString());
        } catch (HabitEvent.DateAlreadyExistsException e) {
            dateEditText.setText("");
            commentEditText.setText("");
            locationPosition = null;
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
            imageView.setImageURI(null);
        } catch (ParseException | DataFormatException e) {
            e.printStackTrace();
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

        EditText commentEditText = (EditText) getActivity().findViewById(R.id.commentEditText);
        try {
            date = sourceFormat.parse(dateEditText.getText().toString());
            comment = commentEditText.getText().toString();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            date = c.getTime();
            habitEvent = new HabitEvent(habit, date, comment);
        }catch(DataFormatException d){
            commentEditText.setError("Comment is too long (20 char max).");
            isValidHabitEvent = false;
        }
        catch (IllegalArgumentException i){
            dateEditText.setError("Invalid date entry");
            Toast.makeText(getActivity(), "Invalid date entry", Toast.LENGTH_LONG).show();
            isValidHabitEvent = false;
        }
        catch (HabitEvent.DateAlreadyExistsException x){
            dateEditText.setError("A HabitEvent already exists on this date");
            Toast.makeText(getActivity(), "A HabitEvent already exists on this date", Toast.LENGTH_LONG).show();
            isValidHabitEvent = false;
        } catch (ParseException e) {
            dateEditText.setError("Please choose another habit");
            Toast.makeText(getActivity(), "Please choose another habit", Toast.LENGTH_LONG).show();
            isValidHabitEvent = false;
        }

        if (isValidHabitEvent) {
            if (image != null){
                habitEvent.setBitmapString(SerializableImage.getStringFromBitmap(image));
            }
            if (locationPosition != null) {
                habitEvent.setLocation(locationPosition);
            }
            habit.getEvents().add(habitEvent);
            // sort on insert
            Collections.sort(habit.getEvents().getArrayList());
            fileController.saveUser(getActivity().getApplicationContext(), user);
            habit.sendToSharedPreferences(getActivity().getApplicationContext());

            sharedPrefs.edit().remove("filteredHabitEvents").apply();

            //set up notification
            if (habit.streak() > 4) {

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                alarmIntent.putExtra("name", habit.getTitle());
                PendingIntent resultPendingIntent =
                        PendingIntent.getBroadcast(
                                getActivity(),
                                habits.indexOf(habit),
                                alarmIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                if (PendingIntent.getBroadcast(getActivity(),habits.indexOf(habit),alarmIntent,PendingIntent.FLAG_NO_CREATE) != null) {
                    alarmManager.cancel(resultPendingIntent);
                    Log.i("Deleted Alarm", habit.getTitle());
                }

                ArrayList<Day> freq = habit.getFrequency();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date today = calendar.getTime();
                if (!date.before(today)) {
                    calendar.add(Calendar.DATE, 1);
                }
                Date dayDate = calendar.getTime();
                SimpleDateFormat sDF = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                for (int i = 0; i < 7; i++) {
                    String dayString = sDF.format(dayDate.getTime()).toUpperCase();
                    if (freq.contains(Day.valueOf(dayString))) {
                        break;
                    }
                    calendar.add(Calendar.DATE, 1);
                    dayDate = calendar.getTime();
                }
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                Log.i("Notification set", calendar.toString());

                alarmManager.set(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), resultPendingIntent);
            }

            // go to view habitevent activity
            Intent intent = new Intent(getActivity(), ViewHabitEventActivity.class);
            intent.putExtra("habitEventDate",habitEvent.getEventDate().toString());
            startActivityForResult(intent, 3);
        }
    }

    /**
     * Check if the user has allowed the app access to read external storage, and if not, request
     * permission.
     * @return int representing the status of the permission
     */
    public int checkReadPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == -1){ // not allowed, so request
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        return ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * This method dictates the action taken when the user responds to a request for the application to access
     * it's files for the purpose of reading images.
     * @param requestCode the request code of the type of request given to the user
     * @param permissions
     * @param grantResults contains data on whether permission was granted
     */
     @Override
     public void onRequestPermissionsResult(int requestCode,
                                            String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    //getImage();
                } else {
                    //permission denied
                }
            }
        }
     }

    /**
     * Method called when the select image button is pressed. Lets the user select an image to be added to the
     * habit event. Calls startActivityForResult to handle their selection.
     * @param view supplied when button is pressed
     */
    public void onSelectImageButtonPress(View view){
        if (checkReadPermission() == -1){
            return;
        }
        getImage();
    }

    private void getImage(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    /**
     * Handles the user selecting an image from their photos. When an image is selected, the filepath
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
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            if (filePath == null){
                // error, they probably didnt use Photos
                Toast.makeText(getActivity(), "Please select an image with the Photos application.", Toast.LENGTH_LONG).show();
                return;
            }
            File file = new File(filePath);
            if (file.length() > 65536){
                Toast.makeText(getActivity(), "Image is too large.", Toast.LENGTH_LONG).show();
                return;
            }
            image = myBitmap;
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == getActivity().RESULT_OK ) {
                getActivity().onBackPressed();
            }
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
