/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Locale;
import java.util.zip.DataFormatException;

import io.searchbox.annotations.JestId;

/**
 * Created by biancaangotti on 2017-10-18.
 */

/**
 * A HabitEvent that records the user performing an instance of a habit.
 * @author amckerna
 * @version 1.0
 */
public class HabitEvent implements Comparable<HabitEvent> {
    private transient Habit habit;
    private String comment = "";
    private Date eventDate;
    // TODO location variable
    private String imageFilePath;
    private SerializableImage image;

    /**
     * Constructor for HabitEvent without the comment.
     * @param habit habit that the habit event is a part of
     * @param eventDate date the habit event takes place
     * @throws IllegalArgumentException throws if date is before the start date of the habit
     */
    public HabitEvent(Habit habit, Date eventDate) throws IllegalArgumentException {
        this.habit = habit;
        //this.habitTitle = habit.getTitle();
        if (habit.getStartDate().after(eventDate)){
            throw new IllegalArgumentException();
        } else if (eventDate.after(new Date())) {
            throw new IllegalArgumentException();
        }
        // TODO: make sure the habit doesnt have any habitevents with this date
        this.eventDate = eventDate;
    }

    /**
     * Constructor for HabitEvent with a comment
     * @param habit habit that the habit event is a part of
     * @param eventDate date the habit event takes place
     * @param comment comment describing the habit event
     * @throws DataFormatException throws if comment is over 20 characters
     * @throws IllegalArgumentException throws if date is before habit start date
     * @throws DateAlreadyExistsException throws if there is a habit event with this date already associated with this habit
     */
    public HabitEvent(Habit habit, Date eventDate, String comment) throws DataFormatException, IllegalArgumentException, DateAlreadyExistsException{
        this.habit = habit;
        if (comment.length() > 20){
            throw new DataFormatException();
        }
        this.comment = comment;
        if (habit.getStartDate().after(eventDate)){
            throw new IllegalArgumentException();
        } else if (eventDate.after(new Date())) {
            throw new IllegalArgumentException();
        }
        for (HabitEvent events: habit.getEvents().getArrayList()){
            if (events.getEventDate().equals(eventDate))
                throw new DateAlreadyExistsException();
        }
        this.eventDate = eventDate;
    }

    /**
     * Exception thrown when a habit event is created on the same day as another habit event associated with the same habit.
     * @author amckerna
     * @version 1.0
     */
    public class DateAlreadyExistsException extends Exception{
        DateAlreadyExistsException(){}

        DateAlreadyExistsException(String message){
            super(message);
        }
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public String getComment() {
        return comment;
    }

    /**
     * setter for comment
     * @param comment
     * @throws DataFormatException throws if comment is more than 20 chars long
     */
    public void setComment(String comment) throws DataFormatException {
        if (comment.length() > 20){
            throw new DataFormatException();
        }
        this.comment = comment;
    }

    public Date getEventDate() {
        return eventDate;
    }

    /**
     * setter for event date
     * @param eventDate
     * @throws IllegalArgumentException throws if event date is before habit start date
     * @throws DateAlreadyExistsException throws if there is a habit event with this date already associated with this habit
     */
    public void setEventDate(Date eventDate) throws IllegalArgumentException, DateAlreadyExistsException {
        if (habit.getStartDate().after(eventDate)){
            throw new IllegalArgumentException();
        } else if (eventDate.after(new Date())) {
            throw new IllegalArgumentException();
        }
        for (HabitEvent events: habit.getEvents().getArrayList()){
            if (this.equals(events)) {
                continue;
            }
            if (events.getEventDate().equals(eventDate))
                throw new DateAlreadyExistsException();
        }
        this.eventDate = eventDate;
    }

    /**
     * Set a path to an image file in storage. Used in getImageBitmap()
     * @param path path to an image file
     */
    public void setImageFilePath(String path){ this.imageFilePath = path; }

    public String getImageFilePath(){ return this.imageFilePath; }

    public void setImage(SerializableImage image){ this.image = image; }

    public SerializableImage getImage(){ return this.image; }

    /**
     * If a path to an image file has been set with setImageFilePath, generates a Bitmap object
     * matching that image.
     * @return a Bitmap object representing the image at the specified location. Returns null otherwise.
     */
    public Bitmap getImageBitmap(){
        if (imageFilePath != null) {
            File imgFile = new File(imageFilePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                return myBitmap;
            }
        }
        return null;
    }

    /**
     * Compares HabitEvents to each other. Calling Collection.sort will sort them in descending order for display on AllHabitEventsFragment
     * @param habitEvent
     * @return
     */
    @Override
    public int compareTo(HabitEvent habitEvent){
        return this.getEventDate().compareTo(habitEvent.getEventDate()) * -1;
    }

    @Override
    public String toString(){
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        return this.getHabit().getTitle() + "     " + formatter.format(eventDate);
    }
}