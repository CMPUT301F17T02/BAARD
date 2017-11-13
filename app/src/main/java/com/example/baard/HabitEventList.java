/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;

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

    public HabitEventList(){
    }

    public HabitEvent getHabitEvent(int index){
        return events.get(index);
    }

    public HabitEvent getHabitEvent(HabitEvent habitEvent){
        int find = events.indexOf(habitEvent);
        return events.get(find);
    }

    public void removeHabitEvent(HabitEvent habitEvent){
        if (events.contains(habitEvent))
            events.remove(habitEvent);
    }

    public boolean hasHabitEvent(HabitEvent event){
        return events.contains(event);
    }

    public void add(HabitEvent event) {
        events.add(event);
    }

    public void delete(HabitEvent event) {
        events.remove(event);
    }

    public ArrayList<HabitEvent> getArrayList(){
        return events;
    }

    public int size(){ return events.size(); }

}
