/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Entities;

import java.util.ArrayList;

/**
 * A class designed to hold a list of HabitEvents.
 * @author amckerna
 * @version 1.0
 */
public class HabitEventList {

    private ArrayList<HabitEvent> events = new ArrayList<>();

    public HabitEventList(){}

    /**
     * Return the HabitEvent at the specified index
     * @param index position in list
     * @return Habit event found
     */
    public HabitEvent getHabitEvent(int index){
        return events.get(index);
    }

    /**
     * Return the HabitEvent matching the given object.
     * @param habitEvent event to get
     * @return Habit Event found
     */
    public HabitEvent getHabitEvent(HabitEvent habitEvent){
        int find = events.indexOf(habitEvent);
        return events.get(find);
    }

    /**
     * Check whether a given HabitEvent is within the list.
     * @param event event to find
     * @return boolean representing whether the HabitEvent is within the list
     */
    public boolean hasHabitEvent(HabitEvent event){
        return events.contains(event);
    }

    /**
     * add a given HabitEvent to the list.
     * @param event event to add
     */
    public void add(HabitEvent event) {
        events.add(event);
    }

    /**
     * remove a specified HabitEvent from the list.
     * @param event event to delete
     */
    public void delete(HabitEvent event) {
        events.remove(event);
    }

    /**
     * return an ArrayList representing the HabitEventList
     * @return ArrayList representing HabitEventList
     */
    public ArrayList<HabitEvent> getArrayList(){
        return events;
    }

    /**
     * return the number of elements of the list as an int.
     * @return size of HabitEventList (number of events)
     */
    public int size(){ return events.size(); }

}
