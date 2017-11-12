/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;

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
public class HabitEvent {
    private transient Habit habit;
    private String comment = "";
    private Date eventDate;
    @JestId
    private String id;
    private String userId;
    private String habitId;
    // TODO location variable
    private Uri image;

    /**
     * Constructor for HabitEvent without the comment.
     * @param habit habit that the habit event is a part of
     * @param eventDate date the habit event takes place
     * @throws IllegalArgumentException throws if date is before the start date of the habit
     */
    public HabitEvent(Habit habit, Date eventDate) throws IllegalArgumentException {
        this.habit = habit;
        if (habit.getStartDate().before(eventDate)){
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
     */
    public HabitEvent(Habit habit, Date eventDate, String comment) throws DataFormatException, IllegalArgumentException{
        this.habit = habit;
        if (comment.length() > 20){
            throw new DataFormatException();
        }
        this.comment = comment;
        if (habit.getStartDate().before(eventDate)){
            throw new IllegalArgumentException();
        }
        // TODO: make sure the habit doesnt have any habitevents with this date
        this.eventDate = eventDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
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
     */
    public void setEventDate(Date eventDate) throws IllegalArgumentException {
        if (habit.getStartDate().after(eventDate)){
            throw new IllegalArgumentException();
        }
        this.eventDate = eventDate;
    }

    public void setImage(Uri imageUri){ this.image = imageUri; }

    public Uri getImage(){ return this.image; }

    @Override
    public String toString(){
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        return this.getHabit().getTitle() + "     " + formatter.format(eventDate);
    }
}