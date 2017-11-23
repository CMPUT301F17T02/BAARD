/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.zip.DataFormatException;

/**
 * Activity that is started when the user pressed the edit button when viewing a HabitEvent
 * @author amckerna
 * @version 1.0
 */
public class EditHabitEventActivity extends AppCompatActivity {

    private Habit habit;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private HabitEvent habitEvent;
    private static final int PICK_IMAGE = 1;
    private String imageFilePath;
    private final FileController fileController = new FileController();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = fileController.loadUser(getApplicationContext(), getUsername());

        // retrieve Habitevent identifier (date)
        String eventDateString = getIntent().getStringExtra("habitEventDate");
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("currentlyViewingHabit", "");
        Habit loadHabit = gson.fromJson(json, new TypeToken<Habit>() {}.getType());
        // do this because we need to set it to the habit found in the user so that the changes are
        // reflected
        for (Habit habits: user.getHabits().getArrayList()){
            if (habits.getTitle().equals(loadHabit.getTitle())){
                habit = habits;
            }
        }
        for (HabitEvent habitEvent1: habit.getEvents().getArrayList()){
            if (habitEvent1.getEventDate().toString().equals(eventDateString)){
                habitEvent = habitEvent1;
                break;
            }
        }
        // set the habit so all methods work properly
        habitEvent.setHabit(habit);
        setContentView(R.layout.activity_edit_habit_event);

        DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        final EditText dateEdit = (EditText) findViewById(R.id.dateEditText);
        dateEdit.setText(formatter.format(habitEvent.getEventDate()));
        final Calendar calendar = Calendar.getInstance();
        dateEdit.setFocusable(false);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        dateEdit.setText(sdf.format(calendar.getTime()));
                    }
                };

                DatePickerDialog d = new DatePickerDialog(EditHabitEventActivity.this, listener, calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMaxDate((new Date()).getTime());
                d.getDatePicker().setMinDate(habit.getStartDate().getTime());
                d.show();
            }
        });

        EditText commentEdit = (EditText) findViewById(R.id.commentEditText);
        commentEdit.setText(habitEvent.getComment());

        Button locationButton = (Button) findViewById(R.id.LocationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "COMING SOON!", Toast.LENGTH_SHORT).show();
            }
        });

        Button imageButton = (Button) findViewById(R.id.selectImageButton);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onSelectImageButtonPress(view);
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveChangesButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveChanges();
            }
        });
    }

    /**
     * returns the username of the user stored in SharedPreferences
     * @return username
     */
    private String getUsername(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        return gson.fromJson(json, new TypeToken<String>() {}.getType());
    }

    /**
     * Check if the user has allowed the app access to read external storage, and if not, request
     * permission.
     * @return int representing the status of the permission
     */
    public int checkReadPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == -1){ // not allowed, so request
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        return permissionCheck;
    }

    /**
     * Check if the user has allowed the app access to read external storage, and if not, request
     * permission.
     * @return int representing the status of the permission
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
                } else {
                    //permission denied
                }
                return;
            }
        }
    }

    /**
     * Save the changes made by the user to this HabitEvent. Checks for errors if the user entered invalid information.
     */
    public void saveChanges() {
        Date date;
        String comment;
        boolean isValidHabitEvent = true;
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        EditText dateEditText = (EditText) findViewById(R.id.dateEditText);
        EditText commentEditText = (EditText) findViewById(R.id.commentEditText);
        try {
            date = sourceFormat.parse(dateEditText.getText().toString());
            comment = commentEditText.getText().toString();
            habitEvent.setEventDate(date);
            habitEvent.setComment(comment);
        } catch (DataFormatException d) {
            commentEditText.setError("Comment is too long (20 char max).");
            isValidHabitEvent = false;
        } catch (IllegalArgumentException i) {
            dateEditText.setError("Date is before habit start date. (" + habit.getStartDate().toString() + ")");
            isValidHabitEvent = false;
        } catch (HabitEvent.DateAlreadyExistsException x){
            dateEditText.setError("A HabitEvent already exists on this date.");
            isValidHabitEvent = false;
        } catch (Exception e) {
            //invalid date format
            dateEditText.setError("Invalid date entry:");
            isValidHabitEvent = false;
        }

        if (isValidHabitEvent) {
            if (imageFilePath != null) {
                habitEvent.setImageFilePath(imageFilePath);
            }
            // sort on change
            Collections.sort(habit.getEvents().getArrayList());
            fileController.saveUser(getApplicationContext(), user);
            habit.sendToSharedPreferences(getApplicationContext());
            // go to view habitevent activity
            Intent intent = new Intent(this, ViewHabitEventActivity.class);
            intent.putExtra("habitEventDate", habitEvent.getEventDate().toString());
            startActivity(intent);
            finish();
        }
    }

    /**
     * Method called when the select image button is pressed. Lets the user select an image to be added to the
     * habit event. Calls startActivityForResult to handle their selection.
     * @param view supplied when button is pressed
     */
    public void onSelectImageButtonPress(View view){
        checkReadPermission();
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
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

            Cursor cursor = this.getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            //TextView textView = (TextView) findViewById(R.id.filenameTextView);
            imageFilePath = filePath;
            //textView.setText(filePath);
            ImageView imageView = (ImageView) findViewById(R.id.imageViewEditEvent);
            imageView.setImageURI(selectedImage);
        }
    }
}
