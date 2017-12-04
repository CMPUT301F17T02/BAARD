/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Entities;

import java.util.ArrayList;

/**
 * List of Habits that a user has
 * @see Habit
 * @see User
 * @since 1.0
 * @version 1.1
 * @author rderbysh, bangotti
 */
public class HabitList {

    private ArrayList<Habit> habits = new ArrayList<Habit>();

    /**
     * Constructor
     */
    public HabitList(){

    }

    /**
     * @param index index in list
     * @return Habit found
     */
    public Habit getHabit(int index){
        return habits.get(index);
    }

    /**
     * @param habit Habit to find
     * @return Habit found
     */
    public Habit getHabit(Habit habit){
        int find = habits.indexOf(habit);
        return habits.get(find);
    }

    /**
     * @param habit Habit to search
     * @return boolean true if found
     */
    public boolean hasHabit(Habit habit){
        return habits.contains(habit);
    }

    /**
     * @param habit Habit to find
     * @return Index in list
     */
    public int indexOf(Habit habit) {
        return habits.indexOf(habit);
    }

    /**
     * @param habit Habit to add
     */
    public void add(Habit habit) {
        habits.add(habit);
    }

    /**
     * @param habit Habit to delete
     */
    public void delete(Habit habit) {
        habits.remove(habit);
    }

    /**
     * @return arrayList representation of HabitList
     */
    public ArrayList<Habit> getArrayList() {return habits;}

    /**
     * @return Integer size of list
     */
    public int size() {
        return habits.size();
    }

}
