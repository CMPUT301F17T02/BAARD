/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.baard.Habits.AllHabitsFragment;
import com.example.baard.Habits.DailyHabitsFragment;
import com.example.baard.Habits.EditHabitActivity;
import com.example.baard.Entities.Habit;
import com.example.baard.Entities.HabitList;
import com.example.baard.R;
import com.example.baard.Entities.User;
import com.example.baard.Habits.ViewHabitActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;


/**
 * Adapter for Expandable List Views in Daily Habits and All Habits Fragments of Main Activity
 * Referenced and copied from https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 * @version 1.0
 * @see DailyHabitsFragment
 * @see AllHabitsFragment
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    public final Context _context;
    private final List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private final HashMap<String, List<String>> _listDataChild;
    private final HabitList allHabitsList;
    private final HabitList seenHabitsList;

    /**
     * Constructor for Expandable List Adapter
     * @param context The context of the Application
     * @param listDataHeader The header names for the list (Habit names)
     * @param listChildData Child Strings (not used in this implementation) Pass in HashMap with Strings of Headers and a List<String> of just ""
     * @param allHabitsList All habits of the user
     * @param seenHabitsList List of Habits on the screen (Daily or All)
     */
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, HabitList allHabitsList, HabitList seenHabitsList) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.allHabitsList = allHabitsList;
        this.seenHabitsList = seenHabitsList;
    }

    /**
     * Gets the child in the given group at the position indicated
     * @param groupPosition given group
     * @param childPosition position in group
     * @return Child object
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    /**
     * Gets the child's ID at the given group at the position indicated
     * @param groupPosition given group
     * @param childPosition position in group
     * @return long of the Child ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Gets the view corresponding to the child to be displayed.
     * Also implements OnClickListeners for buttons within children.
     * @param groupPosition given group
     * @param childPosition position in group
     * @param isLastChild boolean true if last
     * @param convertView View to be used
     * @param parent Group parent
     * @return View of the Child
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_expandable, null);
        }

        convertView.findViewById(R.id.viewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit h = seenHabitsList.getHabit(groupPosition);
                int index = allHabitsList.indexOf(h);
                Intent intent = new Intent(_context, ViewHabitActivity.class);
                intent.putExtra("position", index);
                _context.startActivity(intent);
            }
        });

        convertView.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit h = seenHabitsList.getHabit(groupPosition);
                int index = allHabitsList.indexOf(h);
                Intent intent = new Intent(_context, EditHabitActivity.class);
                intent.putExtra("position", index);
                _context.startActivity(intent);
            }
        });

        convertView.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileController fc = new FileController();
                Habit h = seenHabitsList.getHabit(groupPosition);
                allHabitsList.delete(h);
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("username", "");
                String username = gson.fromJson(json, new TypeToken<String>() {
                }.getType());
                User user = fc.loadUser(_context, username);
                user.setHabits(allHabitsList);
                fc.saveUser(_context, user);
                _listDataHeader.remove(groupPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     * Get count of all Children in given group
     * @param groupPosition given group
     * @return integer count
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    /**
     * Gets the Group Object in the list
     * @param groupPosition the position of the group desired
     * @return Group object
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    /**
     * Get the count of groups List items (Headers)
     * @return integer count
     */
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    /**
     * Get group ID at specified position
     * @param groupPosition specified position
     * @return long of group ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets the view for each group (header)
     * @param groupPosition the position of the current group
     * @param isExpanded boolean true if expanded
     * @param convertView View to be used
     * @param parent Group parent (expandable list view)
     * @return View of the Group
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    /**
     * Determines whether the IDS are stable
     * @return Boolean False (IDs can change)
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Determines if the child is selectable within the given group
     * @param groupPosition given group
     * @param childPosition given child
     * @return Boolean True (All children are selectable)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
