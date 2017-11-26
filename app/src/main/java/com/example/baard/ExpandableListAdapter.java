/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Adam on 11/25/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HabitList allHabitsList;
    private HabitList seenHabitsList;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, HabitList allHabitsList, HabitList seenHabitsList) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.allHabitsList = allHabitsList;
        this.seenHabitsList = seenHabitsList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_expandable, null);
        }

        convertView.findViewById(R.id.viewHabitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit h = seenHabitsList.getHabit(groupPosition);
                int index = allHabitsList.indexOf(h);
                Intent intent = new Intent(_context, ViewHabitActivity.class);
                intent.putExtra("position", index);
                _context.startActivity(intent);
            }
        });

        convertView.findViewById(R.id.editHabitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit h = seenHabitsList.getHabit(groupPosition);
                int index = allHabitsList.indexOf(h);
                Intent intent = new Intent(_context, EditHabitActivity.class);
                intent.putExtra("position", index);
                _context.startActivity(intent);
            }
        });

        convertView.findViewById(R.id.deleteHabitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit h = seenHabitsList.getHabit(groupPosition);
                int index = allHabitsList.indexOf(h);
                FileController fc = new FileController();
                allHabitsList.delete(h);
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("username", "");
                String username = gson.fromJson(json, new TypeToken<String>() {}.getType());
                User user = fc.loadUser(_context, username);
                user.setHabits(allHabitsList);
                fc.saveUser(_context, user);
                _listDataHeader.remove(groupPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
