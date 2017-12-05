/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Habits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baard.Controllers.FileController;
import com.example.baard.Controllers.TypefaceSpan;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitEvent;
import com.example.baard.Entities.HabitList;
import com.example.baard.Entities.User;
import com.example.baard.MainActivity;
import com.example.baard.R;
import com.example.baard.HabitEvents.ViewHabitEventActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Activity to view a habit
 * @see MainActivity
 * @see Habit
 * @see AllHabitsFragment
 * @see DailyHabitsFragment
 * @see AppCompatActivity
 * @author rderbysh, minsoung
 * @since 1.0
 * @version 3.0
 */
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

        setActionBarTitle("View Habit");
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
        TextView titleView = findViewById(R.id.title);
        TextView reasonView = findViewById(R.id.reason);
        TextView startDateView = findViewById(R.id.startDate);
        TextView frequencyView = findViewById(R.id.frequency);
        titleView.setText(habit.getTitle());
        reasonView.setText(habit.getReason());
        startDateView.setText(formatter.format(habit.getStartDate()));
        frequencyView.setText(habit.getFrequencyString());

        createPieChart();
        createBarChart();
        createLineChart();
        listHabitEvents();

        TextView milestoneTextView = findViewById(R.id.milestoneTextView);
        int milestone = habit.milestone();

        if (milestone > 0) {
            milestoneTextView.setText("Milestone reached: \n"+Integer.toString(milestone)+" habit events completed!");
            milestoneTextView.setVisibility(View.VISIBLE);
        } else {
            milestoneTextView.setVisibility(View.GONE);
        }

        TextView streakTextView = findViewById(R.id.streakTextView);
        int streak = habit.streak();
        if (streak > 4) {
            streakTextView.setText("Current Streak: "+Integer.toString(streak));
            streakTextView.setVisibility(View.VISIBLE);
        } else {
            streakTextView.setVisibility(View.GONE);
        }

        Button edit = findViewById(R.id.edit);
        Button delete = findViewById(R.id.delete);

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
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void changeFont() {
        Typeface ralewayRegular = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        TextView titleText = findViewById(R.id.title);
        TextView reasonText = findViewById(R.id.textViewReason);
        TextView startDateText = findViewById(R.id.textViewStartDate);
        TextView freqText = findViewById(R.id.textViewFreq);
        TextView noEventsText = findViewById(R.id.no_events_textView);
        TextView reason = findViewById(R.id.reason);
        TextView startDate = findViewById(R.id.startDate);
        TextView frequency = findViewById(R.id.frequency);
        TextView streakText = findViewById(R.id.streakTextView);
        TextView milestoneText = findViewById(R.id.milestoneTextView);
        Button edit = findViewById(R.id.edit);
        Button delete = findViewById(R.id.delete);

        titleText.setTypeface(ralewayRegular);
        reasonText.setTypeface(ralewayRegular);
        startDateText.setTypeface(ralewayRegular);
        freqText.setTypeface(ralewayRegular);
        noEventsText.setTypeface(ralewayRegular);
        reason.setTypeface(ralewayRegular);
        startDate.setTypeface(ralewayRegular);
        frequency.setTypeface(ralewayRegular);
        streakText.setTypeface(ralewayRegular);
        milestoneText.setTypeface(ralewayRegular);
        edit.setTypeface(ralewayRegular);
        delete.setTypeface(ralewayRegular);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }

    /**
     *  Copied from https://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
     */
    private void setActionBarTitle(String str) {
        String fontPath = "Raleway-Regular.ttf";

        SpannableString s = new SpannableString(str);
        s.setSpan(new TypefaceSpan(this, fontPath), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        getSupportActionBar().setTitle(s);
    }

    /**
     * Ensures the app returns to the proper fragment of main when back pressed
     * @param item the menu item of the toolbar (only home in this case)
     * @return boolean true if success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calculates and creates the Pie chart of events to be displayed
     */
    private void createPieChart() {
        HabitStatistics.HabitCompletionData habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, habit.getStartDate(), new Date());

        // Create Pie Chart
        PieChart pieChart = (PieChart) findViewById(R.id.habit_pieChart);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(1);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setMaxHighlightDistance(0);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        if (habitCompletionData.completed > 0) {
            yValues.add(new PieEntry(habitCompletionData.completed, "Completed On Time"));
        }
        if (habitCompletionData.total - habitCompletionData.completed > 0) {
            yValues.add(new PieEntry(habitCompletionData.total - habitCompletionData.completed, "Not Completed on Time"));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
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
        Log.d("HabitStat", String.valueOf(habitCompletionData.total));
        if (habitCompletionData.total == 0) {
            pieChart.setVisibility(View.GONE);
        } else {
          pieChart.setData(data);
        }

    }

    private void createBarChart() {
        HabitStatistics.HabitCompletionData habitCompletionData = new HabitStatistics().calcHabitCompletion(habit, habit.getStartDate(), new Date());

        // Crate Bar Chart
        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.habit_barChart);
        barChart.setScaleEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries3 = new ArrayList<BarEntry>();
        entries1.add(new BarEntry(0f, habitCompletionData.total));
        entries2.add(new BarEntry(1f, habitCompletionData.late));
        entries3.add(new BarEntry(2f, habitCompletionData.completed));

        BarDataSet dataSet1 = new BarDataSet(entries1, "Total to be completed");
        BarDataSet dataSet2 = new BarDataSet(entries2, "Late");
        BarDataSet dataSet3 = new BarDataSet(entries3, "Completed on time");
        IValueFormatter formatter1 = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + ((int)value);
            }
        };
        dataSet1.setValueFormatter(formatter1);
        dataSet2.setValueFormatter(formatter1);
        dataSet3.setValueFormatter(formatter1);
        dataSet1.setColors(ColorTemplate.MATERIAL_COLORS[1]);
        dataSet2.setColors(ColorTemplate.MATERIAL_COLORS[2]);
        dataSet3.setColors(ColorTemplate.MATERIAL_COLORS[0]);

        BarData data = new BarData();
        data.addDataSet(dataSet1);
        data.addDataSet(dataSet2);
        data.addDataSet(dataSet3);

        if (habitCompletionData.total == 0) {
            barChart.setVisibility(View.GONE);
        } else {
            barChart.setData(data);
        }
        barChart.setFitBars(true);
        barChart.getXAxis().setLabelCount(3);
        barChart.getLegend().setEnabled(true);

        IAxisValueFormatter formatter2 = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        };

        barChart.getXAxis().setValueFormatter(formatter2);
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
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        for (HabitStatistics.HabitCompletionVsTimeData data : habitCompletionVsTimesData) {
            yValues.add(new Entry(data.time, data.habitCompletion));
        }

        LineDataSet set1 = new LineDataSet(yValues, "Habits Completed On Time");
        set1.setDrawValues(false);
        set1.setCircleColor(Color.rgb(0, 153, 76));
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setColor(Color.rgb(0, 153, 76));
        set1.setLineWidth(1f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);
        set1.setFillColor(Color.rgb(0, 153, 76));
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        if (habitCompletionVsTimesData.size() > 0) {
            lineChart.setData(data);
        } else {
            lineChart.setVisibility(View.GONE);
        }


        if (habitCompletionVsTimesData.size() < 5) {
            lineChart.getXAxis().setLabelCount(habitCompletionVsTimesData.size(), true);
        } else {
            lineChart.getXAxis().setLabelCount(5, true);
        }
        //lineChart.getXAxis().setAxisMinimum(habitCompletionVsTimesData.get(0).time - 1000000);
        //lineChart.getXAxis().setAxisMaximum(habitCompletionVsTimesData.get(habitCompletionVsTimesData.size() - 1).time + 1000000);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd",Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long)value);

                return sdf.format(calendar.getTime());
            }
        };

        lineChart.getXAxis().setValueFormatter(formatter);
    }

    private void listHabitEvents() {
        ListView eventsList = findViewById(R.id.habit_events_scroller_ListView);
        final List<HabitEvent> habitEventList = habit.getEvents().getArrayList();

        if (habitEventList.size() > 0) {
            for (HabitEvent e : habitEventList) {
                e.setHabit(habit);
            }

            //ArrayAdapter<HabitEvent> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitEventList);
            ListAdapter adapter = new ListAdapter(this, R.layout.listview_habit, habitEventList);

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

            //eventsList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 450));

            eventsList.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // Disallow the touch request for parent scroll on touch of child view
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        } else {
            eventsList.setVisibility(View.GONE);
            findViewById(R.id.no_events_textView).setVisibility(View.VISIBLE);
        }
    }

    // Copied from https://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
    public class ListAdapter extends ArrayAdapter<HabitEvent> {
        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<HabitEvent> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HabitEvent habitEvent = getItem(position);

            if (convertView == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                convertView = vi.inflate(R.layout.listview_habit, null);
            }

            if (habitEvent != null) {
                TextView date = convertView.findViewById(R.id.date);
                DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                date.setText(formatter.format(habitEvent.getEventDate()));
            }

            return convertView;
        }
    }
}
