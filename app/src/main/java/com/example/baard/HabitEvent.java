/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.Date;

/**
 * Created by biancaangotti on 2017-10-18.
 */

public class HabitEvent {
    private Habit habit;
    private String comment;
    private Date eventDate;
    // TODO location variable
    // TODO picture variable

    public HabitEvent(Habit habit, String comment, Date eventDate) {
        this.habit = habit;
        this.comment = comment;
        this.eventDate = eventDate;
    }

    public HabitEvent(Habit habit, Date eventDate) {
        this.habit = habit;
        this.eventDate = eventDate;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}