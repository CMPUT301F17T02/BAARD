/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


/**
 * Implements the receiver to be called by an Alarm Manager to then instantiate a notification
 *
 * Made with reference to:
 * https://developer.android.com/training/notify-user/build-notification.html#notify
 * https://thenewboston.com/forum/topic.php?id=13015
 *
 * @author anarten
 * @see android.content.BroadcastReceiver
 * @see CreateNewHabitEventFragment
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Sets an ID for the notification
        int mNotificationId = 001;

        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("BAARD Habit Tracker")
                .setContentText("Reminder to complete your "+intent.getExtras().getString("name","streak")+" habit! Don't lose your streak!")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000})
                .setContentIntent(pendingIntent);
        Log.i("ALARM", "Receiver sending notification");
        // Builds the notification and issues it.
        notificationManager.notify(mNotificationId, mNotifyBuilder.build());
    }
}
