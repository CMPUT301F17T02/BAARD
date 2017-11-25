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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.List;
import java.util.Locale;

import static java.lang.Math.min;

public class ViewHabitActivity extends AppCompatActivity {

    private final DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private int position;
    private String username;
    private HabitList habitList;
    private Habit habit;
    private FileController fc;
    private User user;

    /**
     * This create method sets the text based on habit retrieved
     * @param savedInstanceState Bundle for the saved state
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
        listHabitEvents();

        Button edit = (Button) findViewById(R.id.edit);
        Button delete = (Button) findViewById(R.id.delete);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitActivity.this, EditHabitActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habitList.delete(habit);
                fc.saveUser(getApplicationContext(), user);
                finish();
            }
        });

        getSupportActionBar().setTitle("View Habit");
    }

    /**
     * Ensures the app returns to the proper fragment of main when back pressed
     * @param item the menu item of the toolbar (only home in this case)
     * @return boolean true if success
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

        ArrayList<PieEntry> yValues = new ArrayList<>();
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

        ArrayList<Entry> yValues = new ArrayList<>();
        for (HabitStatistics.HabitCompletionVsTimeData data : habitCompletionVsTimesData) {
            yValues.add(new Entry(data.time, data.habitCompletion));
        }

        LineDataSet set1 = new LineDataSet(yValues, "DataSet");

        set1.setColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
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
    private class MyAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd",Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();
            Log.d("LineChart", Float.toString(value));
            calendar.setTimeInMillis((long)value);

            return sdf.format(calendar.getTime());
        }
    }

    private void listHabitEvents() {
        ListView eventsList = (ListView) findViewById(R.id.habit_events_scroller_ListView);
        final List<HabitEvent> habitEventList = habit.getEvents().getArrayList();

        if (habitEventList.size() > 0) {
            for (HabitEvent e : habitEventList) {
                e.setHabit(habit);
            }

            ArrayAdapter<HabitEvent> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitEventList);

            eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //tell the ViewRecordActivity which list item has been selected and start it
                    Intent intent = new Intent(ViewHabitActivity.this, ViewHabitEventActivity.class);
                    intent.putExtra("habitEventDate", habitEventList.get(i).getEventDate().toString());
                    habitEventList.get(i).getHabit().sendToSharedPreferences(getApplicationContext());
                    startActivity(intent);
                }
            });


            eventsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (habitEventList.size()*100 < 300) {
                eventsList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, habitEventList.size()*100));
            } else {
                eventsList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));

                eventsList.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // Disallow the touch request for parent scroll on touch of child view
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
            }
        } else {
            eventsList.setVisibility(View.GONE);
            findViewById(R.id.no_events_textView).setVisibility(View.VISIBLE);
        }
    }
}
