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

        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent habitEvent = habitEventList.getHabitEvent(i);

            if (startDate.compareTo(habitEvent.getEventDate()) >= 0 && endDate.compareTo(habitEvent.getEventDate()) <= 0) {
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

    public ArrayList<HabitCompletionByTimeData> getHabitCompletionByTime(Habit habit, Date startDate, Date endDate) {
        ArrayList<String> datesInStr = new ArrayList<String>();
        ArrayList<Integer> times = new ArrayList<Integer>();
        ArrayList<Integer> habitCompletionsByTime = new ArrayList<Integer>();
        HabitEventList habitEventList = habit.getEvents();
        int offset = 0;
        int completion = 0;

        for (int i = 0; i < habitEventList.size(); i++) {
            HabitEvent habitEvent = habitEventList.getHabitEvent(i);

            if (startDate.compareTo(habitEvent.getEventDate()) >= 0 && endDate.compareTo(habitEvent.getEventDate()) <= 0) {
                completion++;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                datesInStr.add(sdf.format(habitEvent.getEventDate()));
                int time = (int)(habitEvent.getEventDate().getTime() / 1000);
                if (offset > time) {
                    offset = time;
                }
                times.add(time);
                habitCompletionsByTime.add(completion);
            }
        }

        ArrayList<HabitCompletionByTimeData> habitCompletionByTimeData = new ArrayList<HabitCompletionByTimeData>();

        for (int i = 0; i < datesInStr.size(); i++) {
            String dateInStr = datesInStr.get(i);
            int timeWithOffset = times.get(i) - offset;
            int habitCompletionByTime = habitCompletionsByTime.get(i);
            habitCompletionByTimeData.add(new HabitCompletionByTimeData(dateInStr, timeWithOffset, habitCompletionByTime));
        }

        return habitCompletionByTimeData;
    }

    public class HabitCompletionData {
        public final int completed;
        public final int notCompleted;

        public HabitCompletionData(int completed, int notCompleted) {
            this.completed = completed;
            this.notCompleted = notCompleted;
        }
    }

    public class HabitCompletionByTimeData {
        String dateInStr;
        int timeWithOffset;
        int habitCompletion;

        public HabitCompletionByTimeData(String datesInStr, int timeWithOffset, int habitCompletion) {
            this.dateInStr = datesInStr;
            this.timeWithOffset = timeWithOffset;
            this.habitCompletion = habitCompletion;
        }
    }

}
