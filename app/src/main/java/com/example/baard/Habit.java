/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * Created by biancaangotti on 2017-10-18.
 */

public class Habit {
    private String title, reason;
    private Date startDate;
    private ArrayList<Day> frequency;
    private HabitEventList events = new HabitEventList();

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
    @Override
    public String toString() {
        return title;
    }

}

