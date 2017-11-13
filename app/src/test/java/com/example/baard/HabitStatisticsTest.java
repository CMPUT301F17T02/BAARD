/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        Date startDate;
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
        assertEquals(1, habitCompletionData.notCompleted);
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
        assertEquals(1, habitCompletionData.notCompleted);
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
        assertEquals(0, habitCompletionData.notCompleted);
    }

    public void testGetHabitCompletionByTime() {
        ArrayList<HabitStatistics.HabitCompletionByTimeData> habitCompletionByTimesData = null;
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse("08/11/2017");
            endDate   = sdf.parse("10/11/2017");
        } catch (Exception e) {}

        habitCompletionByTimesData = new HabitStatistics().getHabitCompletionByTime(habit, startDate, endDate);

        assertEquals(2, habitCompletionByTimesData.size());
        HabitStatistics.HabitCompletionByTimeData habitCompletionByTimeData0 = habitCompletionByTimesData.get(0);
        HabitStatistics.HabitCompletionByTimeData habitCompletionByTimeData1 = habitCompletionByTimesData.get(1);

        assertEquals("08/11/2017", habitCompletionByTimeData0.dateInStr);
        assertEquals(1, habitCompletionByTimeData0.habitCompletion);
        assertEquals(0, habitCompletionByTimeData0.timeWithOffset);
        assertEquals("10/11/2017", habitCompletionByTimeData1.dateInStr);
        assertEquals(2, habitCompletionByTimeData1.habitCompletion);
    }

}
