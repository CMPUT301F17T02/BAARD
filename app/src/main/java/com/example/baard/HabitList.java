/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import java.util.ArrayList;

/**
 * Created by biancaangotti on 2017-10-20.
 */

public class HabitList {

    private ArrayList<Habit> habits = new ArrayList<Habit>();

    public HabitList(){

    }

    public Habit getHabit(int index){
        return habits.get(index);
    }

    public boolean hasHabit(Habit habit){
        return habits.contains(habit);
    }

    public void add(Habit habit) {
        habits.add(habit);
    }

    public void delete(Habit habit) {
        habits.remove(habit);
    }

    public ArrayList<Habit> getArrayList() {return habits;}

    public int size() {
        return habits.size();
    }

}
