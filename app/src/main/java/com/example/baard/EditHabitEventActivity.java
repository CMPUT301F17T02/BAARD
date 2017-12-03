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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
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
    private Bitmap image;
    private LatLng locationPosition;
    private final FileController fileController = new FileController();
    private User user;
    private SharedPreferences sharedPrefs;
    private Gson gson;
    private final DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private boolean locationExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();

        user = fileController.loadUser(getApplicationContext(), getUsername());

        // retrieve Habitevent identifier (date)
        String eventDateString = getIntent().getStringExtra("habitEventDate");
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

        ImageView image = (ImageView) findViewById(R.id.imageViewEditEvent);
            if (habitEvent.getBitmapString() != null) {
                image.setImageBitmap(SerializableImage.getBitmapFromString(habitEvent.getBitmapString()));
            }

        TextView habitTitle = (TextView) findViewById(R.id.habitTitleTextViewEditEvent);
        habitTitle.setText(habit.getTitle());

        final EditText dateEdit = (EditText) findViewById(R.id.dateEditText);
        dateEdit.setText(sourceFormat.format(habitEvent.getEventDate()));
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
                        dateEdit.setText(sourceFormat.format(calendar.getTime()));
                    }
                };

                DatePickerDialog d = new DatePickerDialog(EditHabitEventActivity.this, listener, calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                if (habit.getStartDate().before(new Date())) {
                    d.getDatePicker().setMaxDate((new Date()).getTime());
                    d.getDatePicker().setMinDate(habit.getStartDate().getTime());
                    d.show();
                } else {
                    Toast.makeText(EditHabitEventActivity.this, "Habit's start date is in the future, please choose another habit", Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText commentEdit = (EditText) findViewById(R.id.commentEditText);
        commentEdit.setText(habitEvent.getComment());

        Button imageButton = (Button) findViewById(R.id.selectImageButton);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onSelectImageButtonPress(view);
            }
        });

        if (habitEvent.getLocation() != null) {
            locationExists = true;
        }

        getSupportActionBar().setTitle("Edit Habit Event");
    }

    @Override
    public void onStart() {
        super.onStart();
        String json = sharedPrefs.getString("locationPosition", "");
        locationPosition = gson.fromJson(json, new TypeToken<LatLng>() {}.getType());
        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton);
        if (locationPosition != null) {
            radioButton.setChecked(true);
            radioButton.setText(R.string.yesLocation);
        } else if (locationExists) {
            radioButton.setChecked(true);
            radioButton.setText(R.string.yesLocation);
        } else {
            radioButton.setChecked(false);
            radioButton.setText(R.string.noLocation);
        }
    }

    /**
     * returns the username of the user stored in SharedPreferences
     * @return username
     */
    private String getUsername(){
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
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
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
            }
        }
    }

    /**
     * Loads add location so user can pick where the event occured
     * @param view
     */
    public void addLocation(View view) {
        Intent intent = new Intent(EditHabitEventActivity.this, AddLocationActivity.class);
        if (habitEvent.getLocation() != null) {
            intent.putExtra("myLocation", habitEvent.getLocation());
        }
        startActivity(intent);
    }

    /**
     * Save the changes made by the user to this HabitEvent. Checks for errors if the user entered invalid information.
     */
    public void saveChanges(View view) {
        Date date;
        String comment;
        boolean isValidHabitEvent = true;

        EditText dateEditText = (EditText) findViewById(R.id.dateEditText);
        EditText commentEditText = (EditText) findViewById(R.id.commentEditText);
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
            if (image != null) {
                habitEvent.setBitmapString(SerializableImage.getStringFromBitmap(image));
            }
            // location can be set to null if user chose to delete it
            habitEvent.setLocation(locationPosition);
            // sort on change
            Collections.sort(habit.getEvents().getArrayList());
            fileController.saveUser(getApplicationContext(), user);
            habit.sendToSharedPreferences(getApplicationContext());
            // go to view habitevent activity
            Intent intent = new Intent();
            intent.putExtra("habitEventDate", habitEvent.getEventDate().toString());
            setResult(RESULT_OK, intent);
            finish();
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
     * Ensures the app returns to the proper fragment of main when back pressed
     * @param item the menu item of the toolbar (only home in this case)
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            if (filePath == null){
                // error, they probably didnt use Photos
                Toast.makeText(this, "Please select an image with the Photos application.", Toast.LENGTH_LONG).show();
                return;
            }
            File file = new File(filePath);
            if (file.length() > 65536){
                Toast.makeText(this, "Image is too large.", Toast.LENGTH_LONG).show();
                return;
            }
            image = myBitmap;
            ImageView imageView = (ImageView) findViewById(R.id.imageViewEditEvent);
            imageView.setImageURI(selectedImage);
        }
    }
}
