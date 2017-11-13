/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniel on 13/11/17.
 */

public class HabitStatistics {

    public HabitCompletionData calcHabitCompletion(Habit habit, Date startDate, Date endDate) {
        HabitEventList habitEventList = habit.getEvents();
        HashSet<Integer> frequency = new HashSet<Integer>();

        int completed = 0;
        int notCompleted = 0;

        for (Day day : habit.getFrequency()) {
            frequency.add(day.getValue());
        }

        System.out.println(habitEventList.size());
        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent habitEvent = habitEventList.getHabitEvent(i);
            if (startDate.compareTo(habitEvent.getEventDate()) <= 0 && endDate.compareTo(habitEvent.getEventDate()) >= 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(habitEvent.getEventDate());
                if (frequency.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                    completed++;
                } else {
                    notCompleted++;
                }
            }
        }

        return new HabitCompletionData(completed, notCompleted);
    }

    public ArrayList<HabitCompletionVsTimeData> getHabitCompletionVsTimeData(Habit habit, Date startDate, Date endDate) {
        ArrayList<Long> times = new ArrayList<Long>();
        ArrayList<Integer> habitCompletions = new ArrayList<Integer>();
        HashSet<Integer> frequency = new HashSet<Integer>();
        HabitEventList habitEventList = habit.getEvents();

        int completion = 0;

        for (Day day : habit.getFrequency()) {
            frequency.add(day.getValue());
        }

        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent habitEvent = habitEventList.getHabitEvent(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(habitEvent.getEventDate());

            if (startDate.compareTo(habitEvent.getEventDate()) <= 0 && endDate.compareTo(habitEvent.getEventDate()) >= 0
                    && frequency.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                completion++;
                long time = habitEvent.getEventDate().getTime();
                times.add(time);
                habitCompletions.add(completion);
            }
        }

        ArrayList<HabitCompletionVsTimeData> habitCompletionVsTimeDatas = new ArrayList<HabitCompletionVsTimeData>();

        for (int i = 0; i < times.size(); i++) {
            long time = times.get(i);
            int habitCompletion = habitCompletions.get(i);
            habitCompletionVsTimeDatas.add(new HabitCompletionVsTimeData(time, habitCompletion));
        }

        return habitCompletionVsTimeDatas;

    }

    public class HabitCompletionData {
        public final int completed;
        public final int notCompleted;

        public HabitCompletionData(int completed, int notCompleted) {
            this.completed = completed;
            this.notCompleted = notCompleted;
        }
    }

    public class HabitCompletionVsTimeData {
        long time;
        int habitCompletion;

        public HabitCompletionVsTimeData(long time, int habitCompletion) {
            this.time = time;
            this.habitCompletion = habitCompletion;
        }
    }

}
