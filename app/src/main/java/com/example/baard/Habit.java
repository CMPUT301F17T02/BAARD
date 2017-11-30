/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.zip.DataFormatException;

import io.searchbox.annotations.JestId;

/**
 * Created by biancaangotti on 2017-10-18.
 */

public class Habit {
    private String title, reason;
    private Date startDate;
    private ArrayList<Day> frequency;
    private HabitEventList events = new HabitEventList();
    @JestId
    private String id;
    private String userId;

    /**
     * Constructor for Habit
     *
     * @param title
     * @param reason
     * @param startDate
     * @param frequency
     * @throws DataFormatException
     */
    public Habit(String title, String reason, Date startDate, ArrayList<Day> frequency) throws DataFormatException {
        if (title.length() <= 20) {
            this.title = title;
        } else {
            throw new DataFormatException();
        }
        if (reason.length() <= 30) {
            this.reason = reason;
        } else {
            throw new DataFormatException();
        }
        this.startDate = startDate;
        this.frequency = frequency;
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

    /**
     * Returns title for habit
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title for habit; if over character limit, returns exception
     *
     * @param title
     * @throws DataFormatException
     */
    public void setTitle(String title) throws DataFormatException {
        if (title.length() <= 20) {
            this.title = title;
        } else {
            throw new DataFormatException("Title over 20 characters.");
        }
    }

    /**
     * Returns reason for habit
     *
     * @return
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets reason for habit; if over character limit, returns exception
     *
     * @param reason
     * @throws DataFormatException
     */
    public void setReason(String reason) throws DataFormatException {
        if (reason.length() <= 30) {
            this.reason = reason;
        } else {
            throw new DataFormatException("Reason over 30 characters.");
        }
    }

    /**
     * Returns date for habit
     *
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets date for habit
     *
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns frequency for habit
     *
     * @return
     */
    public ArrayList<Day> getFrequency() {
        return frequency;
    }

    /**
     * Sets frequency for habit
     *
     * @param frequency
     */
    public void setFrequency(ArrayList<Day> frequency) {
        this.frequency = frequency;
    }

    /**
     * Returns frequency for habit in the form of a string
     *
     * @return
     */
    public String getFrequencyString() {
        String days = "";
        for (int i = 0; i < frequency.size(); i++) {
            // alternate string replacement
            //  StringBuilder day = new StringBuilder(frequency.get(i).toString());
            //  days += day.replace(1, day.length(), day.substring(1).toLowerCase());
            days += frequency.get(i).toString().substring(0,1) + frequency.get(i).toString().substring(1,3).toLowerCase();
            if (i + 1 < frequency.size())
                days += ", ";
        }
        return days;
    }

    /**
     * Returns habit events for habit
     *
     * @return
     */
    public HabitEventList getEvents() {
        return events;
    }

    /**
     * Sets entire list of habit events for habit
     *
     * @param events
     */
    public void setEvents(HabitEventList events) {
        this.events = events;
    }

    /**
     * Returns habit in a specific string
     *
     * @return
     */

    public void sendToSharedPreferences(Context context){
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        sharedPrefsEditor.putString("currentlyViewingHabit", json);
        sharedPrefsEditor.commit();
    }

    /**
     * Determines whether this habit is on a streak and returns the length in number of days
     * @return Boolean true if streak is current
     */
    public int streak() {
        SimpleDateFormat sDF = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        calendar.set(Calendar.DST_OFFSET, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.YEAR, start.get(Calendar.YEAR));
        int streak = 0;
        while ( !calendar.equals(start) ) {
            calendar.add(Calendar.DATE, -1);
            String date = sDF.format(calendar.getTime().getTime()).toUpperCase();
            Day day = Day.valueOf(date);
            if (frequency.contains(day)) {
                boolean found = false;
                for (HabitEvent event: events.getArrayList()){
                    if (event.getEventDate().equals(calendar.getTime())) {
                        streak++;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return streak;
                }
            }
        }
        return streak;
    }

    /**
     * Determines if this habit has achieved a milestone of 5, 10, 25, 50, or 100 overall events.
     * @return integer representing the last milestone achieved. Otherwise 0.
     */
    public int milestone() {
        ArrayList<Integer> milestones = new ArrayList<>();
        milestones.add(5);
        milestones.add(10);
        milestones.add(25);
        milestones.add(50);
        milestones.add(100);

        int toReturn = 0;
        int count = events.size();
        for (Integer m : milestones) {
            if (count < m) {
                return toReturn;
            } else {
                toReturn = m;
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return title;
    }

}

