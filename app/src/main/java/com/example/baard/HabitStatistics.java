/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniel on 13/11/17.
 */

public class HabitStatistics {

    /**
     * Calculates the completion of a habit based on the start and end dates, events and frequency
     * @param habit
     * @return
     */
    public HabitCompletionData calcHabitCompletion(Habit habit) {
        HabitEventList habitEventList = habit.getEvents();
        HashSet<Integer> frequency = new HashSet<Integer>();
        Date startDate = habit.getStartDate();
        Date endDate = new Date();

        for (Day day : habit.getFrequency()) {
            frequency.add(day.getValue());
        }

        int completed = 0;
        int late = 0;
        int total = getDaysOfWeekBetweenDates(startDate, endDate, frequency);

        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent habitEvent = habitEventList.getHabitEvent(i);
            if (startDate.compareTo(habitEvent.getEventDate()) <= 0 && endDate.compareTo(habitEvent.getEventDate()) >= 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(habitEvent.getEventDate());
                if (frequency.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                    completed++;
                } else {
                    late++;
                }
            }
        }

        return new HabitCompletionData(completed, late, total);
    }

    private int getDaysOfWeekBetweenDates(Date startDate, Date endDate, HashSet<Integer> daysOfWeek) {
        int days = 0;

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal = endCal;
        }

        do {
            if (daysOfWeek.contains(startCal.get(Calendar.DAY_OF_WEEK))) {
                days++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return days;
    }

    /**
     * Calculate the habit completion over time based on the start and end dates, events and frequency
     * @param habit
     * @param startDate
     * @param endDate
     * @return
     */
    public ArrayList<HabitCompletionVsTimeData> getHabitCompletionVsTimeData(Habit habit, Date startDate, Date endDate) {
        ArrayList<Long> times = new ArrayList<Long>();
        ArrayList<Integer> habitCompletions = new ArrayList<Integer>();
        HashSet<Integer> frequency = new HashSet<Integer>();
        ArrayList<HabitEvent> habitEventList = habit.getEvents().getArrayList();
        Collections.sort(habitEventList, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent event1, HabitEvent event2) {
                return event1.getEventDate().compareTo(event2.getEventDate());
            }
        });

        int completion = 0;

        for (Day day : habit.getFrequency()) {
            frequency.add(day.getValue());
        }

        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent habitEvent = habitEventList.get(i);
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

    /**
     * Class to define the habit completion data
     */
    public class HabitCompletionData {
        public final int completed;
        public final int late;
        public final int total;

        public HabitCompletionData(int completed, int late, int total) {
            this.completed = completed;
            this.late = late;
            this.total = total;
        }
    }

    /**
     * Class to define the habit completion versus time
     */
    public class HabitCompletionVsTimeData {
        long time;
        int habitCompletion;

        public HabitCompletionVsTimeData(long time, int habitCompletion) {
            this.time = time;
            this.habitCompletion = habitCompletion;
        }
    }

}
