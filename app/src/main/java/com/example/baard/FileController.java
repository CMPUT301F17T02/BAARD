/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
import java.util.concurrent.ExecutionException;

/**
 * Controls JSON Serialization and Elastic Searches for loading and saving users
 * @see ElasticSearchController
 * @author anarten
 * @since 1.0
 * @version 1.1
 */
public class FileController {

    private static final String FILENAME = "BAARD.sav";

    /**
     * Constructor for FileController
     */
    public FileController() {
    }

    /**
     * Check for internet connection
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @return Boolean true if Network is available
     */
    public boolean isNetworkAvailable(Context context) {
        // Taken from https://stackoverflow.com/questions/30343011/how-to-check-if-an-android-device-is-online
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    /**
     * Stores the user both locally and server if network is available
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @param user The user to be stored
     */
    public void saveUser(Context context, User user) {
        saveUserToFile(context, user);
        if (isNetworkAvailable(context)) {
            saveUserToServer(user);
        }
    }

    /**
     * Load user from server if network is available else from local file
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @return The user stored
     */
    public User loadUser(Context context, String username) {
        User fileUser = loadUserFromFile(context);
        if (isNetworkAvailable(context)) {
            User serverUser = loadUserFromServer(username);
            if (fileUser != null) {
                fileUser.setReceivedRequests(serverUser.getReceivedRequests());
                fileUser.setFriends(serverUser.getFriends());
            } else {
                fileUser = serverUser;
            }
            saveUser(context, fileUser);
        }
        return fileUser;
    }

    /**
     * Loads the user stored locally on the device
     * Taken from lonelytwitter lab exercises
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @return User stored in the file
     */
    private User loadUserFromFile(Context context) {
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
    private void saveUserToFile(Context context, User user) {
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


    /**
     * Deletes user file locally when account is deleted.
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @return boolean true if successful
     */
    public boolean deleteFile(Context context) {
        return context.deleteFile(FILENAME);
    }

    /**
     * Load the user from the server
     * @return User stored on server
     */
    private User loadUserFromServer(String username) {
        ElasticSearchController.GetUserTask getUserTask = new ElasticSearchController.GetUserTask();
        getUserTask.execute(username);
        User user;
        try {
            user = getUserTask.get();
        } catch (ExecutionException | InterruptedException e) {
            user = null;
        }
        return user;
    }

    /**
     * Save user to server
     * @param user The user to be saved
     */
    private void saveUserToServer(User user) {
        ElasticSearchController.UpdateUserTask updateUserTask = new ElasticSearchController.UpdateUserTask();
        updateUserTask.execute(user);
    }

    /**
     * Saves the received requests list of the desired friend
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @param myUsername String username of the current user
     * @param friendUsername String username of the friend we're trying to follow
     * @return boolean true if other user found; false if friend not found.
     */
    public boolean sendFriendRequest(Context context, String myUsername, String friendUsername) {
        User friend = loadUserFromServer(friendUsername);
        User me = loadUser(context, myUsername);
        if (friend != null) {
            friend.getReceivedRequests().add(me);
            saveUserToServer(friend);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Accepts the pending friend request
     * @param context The Application Context at the time of calling. Use getApplicationContext()
     * @param myUsername String username of the current user
     * @param friendUsername String username of the friend trying to follow me
     * @return boolean true if other user found; false if not found.
     */
    public boolean acceptFriendRequest(Context context, String myUsername, String friendUsername) {
        User friend = loadUserFromServer(friendUsername);
        User me = loadUser(context, myUsername);
        if (friend != null) {
            me.getReceivedRequests().delete(friend);
            friend.getFriends().add(me);
            return true;
        } else {
            return false;
        }
    }
}
