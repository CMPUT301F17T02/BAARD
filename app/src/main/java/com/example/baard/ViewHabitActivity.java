/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ViewHabitActivity extends AppCompatActivity {

    private DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private int position;
    private String username;
    private HabitList habitList;
    private Habit habit;
    private FileController fc;
    private User user;

    /**
     * This create method sets the text based on habit retrieved
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        fc = new FileController();

        // grab the index of the item in the list
        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        username = gson.fromJson(json, new TypeToken<String>() {}.getType());

    }

    /**
     * Load the user to display the updated habit
     */
    @Override
    public void onStart() {
        super.onStart();

        // load required data
        user = fc.loadUser(getApplicationContext(), username);
        habitList = user.getHabits();
        habit = habitList.getHabit(position);

        // set all of the values for the habit to be viewed
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView reasonView = (TextView) findViewById(R.id.reason);
        TextView startDateView = (TextView) findViewById(R.id.startDate);
        TextView frequencyView = (TextView) findViewById(R.id.frequency);
        titleView.setText(habit.getTitle());
        reasonView.setText(habit.getReason());
        startDateView.setText(formatter.format(habit.getStartDate()));
        frequencyView.setText(habit.getFrequencyString());

        createPieChart();
        createLineChart();
    }

    /**
     * Ensures the app returns to the proper fragment of main when back pressed
     * @param item the menu item of the toolbar (only home in this case)
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user taps the Edit button.
     * Sends data for user to edit.
     *
     * @param view
     */
    public void editHabit(View view) {
        Intent intent = new Intent(this, EditHabitActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Delete button.
     * Removes habit from user's habit list.
     *
     * @param view
     */
    public void deleteHabit(View view) {
        habitList.delete(habit);
        fc.saveUser(getApplicationContext(), user);
        finish();
    }

    /**
     * Calculates and creates the Pie chart of events to be displayed
     */
    private void createPieChart() {
        HabitStatistics.HabitCompletionData habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, new Date(Long.MIN_VALUE), new Date());

        // Create Pie Chart
        PieChart pieChart = (PieChart) findViewById(R.id.habit_pieChart);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setMaxHighlightDistance(0);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        yValues.add(new PieEntry(habitCompletionData.completed, "Completed"));
        yValues.add(new PieEntry(habitCompletionData.notCompleted, "Not Completed"));

        PieDataSet dataSet = new PieDataSet(yValues, "# of Habits");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + ((int)value);
            }
        });

        PieData data = new PieData(dataSet);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.setData(data);
        if (habitCompletionData.notCompleted == 0 && habitCompletionData.completed == 0) {
            pieChart.setVisibility(View.GONE);
        } else {
          pieChart.setData(data);
        }

    }


    /**
     * Calculates and creates the line chart of events to be displayed
     */
    private void createLineChart() {
        final ArrayList<HabitStatistics.HabitCompletionVsTimeData> habitCompletionVsTimesData = new HabitStatistics().getHabitCompletionVsTimeData(habit, new Date(Long.MIN_VALUE), new Date());

        // Create Line Chart
        LineChart lineChart = (LineChart) findViewById(R.id.habit_lineChart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (HabitStatistics.HabitCompletionVsTimeData data : habitCompletionVsTimesData) {
            yValues.add(new Entry(data.time, data.habitCompletion));
        }

        LineDataSet set1 = new LineDataSet(yValues, "DataSet");

        set1.setColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        if (habitCompletionVsTimesData.size() > 0) {
            lineChart.setData(data);
        } else {
            lineChart.setVisibility(View.GONE);
        }

        lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter());
        lineChart.getXAxis().setGranularity(1f);
    }

    /**
     * Formats the scale of the x axis for the line chart
     */
    public class MyAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            Calendar calendar = Calendar.getInstance();
            Log.d("LineChart", Float.toString(value));
            calendar.setTimeInMillis((long)value);

            return sdf.format(calendar.getTime());
        }
    }
}
