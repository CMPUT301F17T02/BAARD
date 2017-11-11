/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewHabitEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pass this habitevent via intent
        HabitEvent habitEvent = null;

        setContentView(R.layout.activity_view_habit_event);
        TextView name = (TextView) findViewById(R.id.HabitName);
        name.setText(habitEvent.getHabit().getTitle());
        TextView date = (TextView) findViewById(R.id.HabitEventDate);
        date.setText(habitEvent.getEventDate().toString());
        TextView comment = (TextView) findViewById(R.id.commentView);
        comment.setText(habitEvent.getComment());
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageURI(habitEvent.getImage());
        //set onClick listeners for the edit/delete buttons
        Button deleteButton = (Button) findViewById(R.id.DeleteHabitEventButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteHabitEvent();
            }
        });
        Button editButton = (Button) findViewById(R.id.EditHabitEventButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                editHabitEvent();
            }
        });
    }

    private void deleteHabitEvent(){
        //delete this habit event from the Habit's HabitEventList
    }

    private void editHabitEvent(){
        //call a new activity for editing this bizz
    }
}
