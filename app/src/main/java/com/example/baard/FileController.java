/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Created by Adam on 11/11/2017.
 */

class FileController {

    private static final String FILENAME = "BAARD.sav";

    /**
     * Constructor for FileController
     */
    public FileController() {
    }

    // TODO: Check for device online and push to server.

    /**
     * Loads the user stored locally on the device
     * Taken from lonelytwitter lab exercises
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @return User stored in the file
     */
    public User loadUserFromFile(Context context) {
        User user = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type UserType = new TypeToken<User>() {}.getType();
            user = gson.fromJson(in,UserType);
            //https://github.com/google/gson/blob/master/UserGuide.md#TOC-Collections-Examples
        } catch (FileNotFoundException ignored) {
        }
        return user;
    }

    /**
     * Saves the specified user to file locally
     * Taken from lonelytwitter lab exercises
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @param user User to be stored in the file
     */
    public void saveUserToFile(Context context, User user) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(user,writer);
            writer.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
