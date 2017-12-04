/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import com.example.baard.Entities.Day;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitEvent;
import com.example.baard.Habits.HabitStatistics;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by daniel on 13/11/17.
 */

public class HabitStatisticsTest extends TestCase {
    private Habit habit;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public void setUp() {
        ArrayList<Day> frequency = new ArrayList<Day>();
        frequency.add(Day.MONDAY);
        frequency.add(Day.WEDNESDAY);
        frequency.add(Day.FRIDAY);
        try {
            habit = new Habit("Practicing guitar", "To get better", sdf.parse("01/11/2017"), frequency);
            habit.getEvents().add(new HabitEvent(habit, sdf.parse("01/11/2017")));
            habit.getEvents().add(new HabitEvent(habit, sdf.parse("03/11/2017")));
            habit.getEvents().add(new HabitEvent(habit, sdf.parse("06/11/2017")));
            habit.getEvents().add(new HabitEvent(habit, sdf.parse("08/11/2017")));
            habit.getEvents().add(new HabitEvent(habit, sdf.parse("09/11/2017")));
            habit.getEvents().add(new HabitEvent(habit, sdf.parse("10/11/2017")));
        } catch (Exception e) {}
    }

    public void testCalcHabitCompletionAllWithinStartEnd() {
        HabitStatistics.HabitCompletionData habitCompletionData = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("01/11/2017");
            endDate   = sdf.parse("10/11/2017");
        } catch (Exception e) {}
        habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, startDate, endDate);

        assertEquals(5, habitCompletionData.completed);
        assertEquals(1, habitCompletionData.late);
        assertEquals(5, habitCompletionData.total);
    }

    public void testCalcHabitCompletionOneOutFromStart() {
        HabitStatistics.HabitCompletionData habitCompletionData = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("02/11/2017");
            endDate   = sdf.parse("10/11/2017");
        } catch (Exception e) {}
        habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, startDate, endDate);

        assertEquals(4, habitCompletionData.completed);
        assertEquals(1, habitCompletionData.late);
        assertEquals(4, habitCompletionData.total);
    }

    public void testCalcHabitCompletionOneOutFromEnd() {
        HabitStatistics.HabitCompletionData habitCompletionData = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("01/11/2017");
            endDate   = sdf.parse("08/11/2017");
        } catch (Exception e) {}
        habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, startDate, endDate);

        assertEquals(4, habitCompletionData.completed);
        assertEquals(0, habitCompletionData.late);
        assertEquals(4, habitCompletionData.total);
    }

    public void testCalcHabitCompletionNone() {
        HabitStatistics.HabitCompletionData habitCompletionData = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("01/11/2015");
            endDate   = sdf.parse("08/11/2015");
        } catch (Exception e) {}
        habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, startDate, endDate);

        assertEquals(0, habitCompletionData.completed);
        assertEquals(0, habitCompletionData.late);
        assertEquals(3, habitCompletionData.total);
    }

    public void testGetHabitCompletionVsTime() {
        ArrayList<HabitStatistics.HabitCompletionVsTimeData> habitCompletionVsTimeDatas = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("08/11/2017");
            endDate   = sdf.parse("10/11/2017");
        } catch (Exception e) {}

        habitCompletionVsTimeDatas = new HabitStatistics().getHabitCompletionVsTimeData(habit, startDate, endDate);

        assertEquals(2, habitCompletionVsTimeDatas.size());
        HabitStatistics.HabitCompletionVsTimeData habitCompletionByTimeData0 = habitCompletionVsTimeDatas.get(0);
        HabitStatistics.HabitCompletionVsTimeData habitCompletionByTimeData1 = habitCompletionVsTimeDatas.get(1);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(habitCompletionByTimeData0.time);
        assertEquals(1, habitCompletionByTimeData0.habitCompletion);
        assertEquals("08/11/2017", sdf.format(calendar.getTime()));

        calendar.setTimeInMillis(habitCompletionByTimeData1.time);
        assertEquals(2, habitCompletionByTimeData1.habitCompletion);
        assertEquals("10/11/2017", sdf.format(calendar.getTime()));
    }

    public void testGetHabitCompletionVsTimeNone() {
        ArrayList<HabitStatistics.HabitCompletionVsTimeData> habitCompletionVsTimeDatas = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("08/11/2015");
            endDate   = sdf.parse("10/11/2015");
        } catch (Exception e) {}

        habitCompletionVsTimeDatas = new HabitStatistics().getHabitCompletionVsTimeData(habit, startDate, endDate);

        assertEquals(0, habitCompletionVsTimeDatas.size());
    }
}
