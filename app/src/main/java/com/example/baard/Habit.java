/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by biancaangotti on 2017-10-18.
 */

public class Habit {
    private String title, reason;
    private Date startDate;
    private ArrayList<Day> frequency;
    private HabitEventList events = new HabitEventList();

    public Habit(String title, String reason, Date startDate, ArrayList<Day> frequency) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.frequency = frequency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ArrayList<Day> getFrequency() {
        return frequency;
    }

    public void setFrequency(ArrayList<Day> frequency) {
        this.frequency = frequency;
    }

    public HabitEventList getEvents() {
        return events;
    }

    public void setEvents(HabitEventList events) {
        this.events = events;
    }

}

