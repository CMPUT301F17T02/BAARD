/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Entities;

import com.google.android.gms.maps.model.LatLng;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.DataFormatException;

/**
 * A HabitEvent that records the user performing an instance of a habit.
 * @author amckerna
 * @version 1.0
 */
public class HabitEvent implements Comparable<HabitEvent> {
    private transient Habit habit;
    private String comment = "";
    private LatLng location;
    private String eventDate;
    private String bitmapString;

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.eventDate = sdf.format(eventDate);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.eventDate = sdf.format(eventDate);
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

    public String getBitmapString() {
        return bitmapString;
    }

    public void setBitmapString(String bitmapString) {
        this.bitmapString = bitmapString;
    }

    /**
     * setter for comment
     * @param comment new comment
     * @throws DataFormatException throws if comment is more than 20 chars long
     */
    public void setComment(String comment) throws DataFormatException {
        if (comment.length() > 20){
            throw new DataFormatException();
        }
        this.comment = comment;
    }

    public Date getEventDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            return sdf.parse(eventDate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * setter for event date
     * @param eventDate new event date
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.eventDate = sdf.format(eventDate);
    }

    /**
     * Return location of habit event
     *
     * @return location
     */
    public LatLng getLocation() {
        return location;
    }

    /**
     * Set the optional location for a habit event based on user dropping pin
     *
     * @param location new location
     */
    public void setLocation(LatLng location) {
        this.location = location;
    }

    /**
     * Compares HabitEvents to each other. Calling Collection.sort will sort them in descending order for display on AllHabitEventsFragment
     * @param habitEvent other event to compare
     * @return integer of comparison
     */
    @Override
    public int compareTo(HabitEvent habitEvent){
        return this.getEventDate().compareTo(habitEvent.getEventDate()) * -1;
    }

    /**
     * @return String representation of HabitEvent
     */
    @Override
    public String toString(){
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            date = sdf.parse(eventDate);
        } catch (Exception e) {
            date = null;
        }
        DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        return this.getHabit().getTitle() + "\n" + formatter.format(date);
    }
}
