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
     * Load user
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

    private void createPieChart() {
        // Create Pie Chart
        PieChart pieChart = (PieChart) findViewById(R.id.habit_pieChart);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setMaxHighlightDistance(0);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        yValues.add(new PieEntry(6, "Completed"));
        yValues.add(new PieEntry(3, "Not Completed"));

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

    }

    private void createLineChart() {
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
        yValues.add(new Entry(0f, 4f));
        yValues.add(new Entry(1f, 4f));
        yValues.add(new Entry(2f, 4f));

        LineDataSet set1 = new LineDataSet(yValues, "DataRed Set 1");

        set1.setFillAlpha(110);

        set1.setColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);

        String[] xValues = new String[] {"1", "2", "3"};
        lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(xValues));
        lineChart.getXAxis().setGranularity(1f);
    }

    public class MyAxisValueFormatter implements IAxisValueFormatter {

        private String[] xValues;

        public MyAxisValueFormatter(String[] xValues) {
            this.xValues = xValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return xValues[(int)value];
        }
    }
}
