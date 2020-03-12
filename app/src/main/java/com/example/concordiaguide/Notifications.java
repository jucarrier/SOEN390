package com.example.concordiaguide;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.sql.Time;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Notifications {
    public static final String DEFAULT_TITLE = "Time to leave for class";
    public static final int DEFAULT_LEAD_TIME = 8;  //time in minutes before the user needs to leave that the notification will send
    public static final String DEFAULT_MESSAGE = "Fill this in with specific date and time";

    public static void queueNotification(String title, String className, Date dateTime, int leadTime){

    }

}
