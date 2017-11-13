/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

/**
 * Created by biancaangotti on 2017-10-21.
 *
 * Created enum to indicate what day of the week
 * each habit event is taking place on.
 */

public enum Day {
    SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4),
    THURSDAY(5), FRIDAY(6), SATURDAY(7);

    private final int day;

    Day(int day) {
        this.day = day;
    }

    public int getValue() {
        return day;
    }
}
