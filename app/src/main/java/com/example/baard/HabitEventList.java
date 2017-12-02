/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by biancaangotti on 2017-10-20.
 */

/**
 * A class designed to hold a list of HabitEvents.
 * @author amckerna
 * @version 1.0
 */
public class HabitEventList {

    private ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();

    public HabitEventList(){}

    /**
     * Return the HabitEvent at the specified index
     * @param index
     * @return
     */
    public HabitEvent getHabitEvent(int index){
        return events.get(index);
    }

    /**
     * Return the HabitEvent matching the given object.
     * @param habitEvent
     * @return
     */
    public HabitEvent getHabitEvent(HabitEvent habitEvent){
        int find = events.indexOf(habitEvent);
        return events.get(find);
    }

    /**
     * Check whether a given HabitEvent is within the list.
     * @param event
     * @return boolean representing whether the HabitEvent is within the list
     */
    public boolean hasHabitEvent(HabitEvent event){
        return events.contains(event);
    }

    /**
     * add a given HabitEvent to the list.
     * @param event
     */
    public void add(HabitEvent event) {
        events.add(event);
    }

    /**
     * remove a specified HabitEvent from the list.
     * @param event
     */
    public void delete(HabitEvent event) {
        events.remove(event);
    }

    /**
     * return an ArrayList representing the HabitEventList
     * @return
     */
    public ArrayList<HabitEvent> getArrayList(){
        return events;
    }

    /**
     * return the number of elements of the list as an int.
     * @return
     */
    public int size(){ return events.size(); }

}
