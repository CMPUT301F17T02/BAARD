/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by biancaangotti on 2017-10-18.
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
    // TODO picture variable

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

    public HabitEvent(Habit habit, Date eventDate) {
        this.habit = habit;
        this.eventDate = eventDate;
    }

    public HabitEvent(Habit habit, Date eventDate, String comment) {
        this.habit = habit;
        this.comment = comment;
        this.eventDate = eventDate;
    }

    /*
    public HabitEvent(Habit habit, Date eventDate, Picture picture) {
        this.habit = habit;
        this.eventDate = eventDate;
        this.picture = picture;
    }

    public HabitEvent(Habit habit, Date eventDate, Location location) {
        this.habit = habit;
        this.eventDate = eventDate;
        this.picture = picture;
    }

    public HabitEvent(Habit habit, Date eventDate, String comment, Location location) {
        this.habit = habit;
        this.eventDate = eventDate;
        this.comment = comment;
        this.picture = picture;
        this.location = location;
    }

    public HabitEvent(Habit habit, Date eventDate, String comment, Picture picture) {
        this.habit = habit;
        this.eventDate = eventDate;
        this.comment = comment;
        this.picture = picture;
        this.location = location;
    }

    public HabitEvent(Habit habit, Date eventDate, Picture picture, Location location) {
        this.habit = habit;
        this.eventDate = eventDate;
        this.comment = comment;
        this.picture = picture;
        this.location = location;
    }

    public HabitEvent(Habit habit, Date eventDate, String comment, Picture picture, Location location) {
        this.habit = habit;
        this.eventDate = eventDate;
        this.comment = comment;
        this.picture = picture;
        this.location = location;
    }
    */

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

    /*
    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    */
}