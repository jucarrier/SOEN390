package com.example.concordiaguide;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Helpers.CalendarEventDisplayAdapter;
import Helpers.ClassSchedule;
import Models.CalendarEvent;
import Models.CalendarEventDisplayCard;

public class ClassScheduleActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NOTIFICATIONS_ACTIVE = "notificationsActive";
    public ArrayList<Integer> activeAlarmIds = new ArrayList<>();

    public Cursor cursor;   //cursor is needed to access events from google calendar
    public ClassSchedule schedule = new ClassSchedule(new ArrayList<CalendarEvent>()); //create an empty schedule to work with
    public boolean notificationsActive = false;
    public String eventsRead = ""; //used for testing to make sure readEvents has run
    public FloatingActionButton buttonToggleNotifications;
    public TextView notificationsOnOrOff;
    public FloatingActionButton refreshButton;
    //recyclerview to display calendar events to the user
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        buttonToggleNotifications = findViewById(R.id.buttonToggleNotifications);
        notificationsOnOrOff = findViewById(R.id.textViewNotificationsOnOrOff);
        refreshButton = findViewById(R.id.buttonRefreshCalendar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton buttonToggleNotifications = findViewById(R.id.buttonToggleNotifications);
        final TextView notificationsOnOrOff = findViewById(R.id.textViewNotificationsOnOrOff);
        FloatingActionButton refreshButton = findViewById(R.id.buttonRefreshCalendar);


        //permission check to read calendar events
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
            return;
        }

        //cursor that reads the calendar events
        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);

        //read the events in the calendar as soon as the page opens
        readEvents();

        //load whether the user has notifications on or off
        loadPreference();

        //button to refresh the calendar when new events are added
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readEvents();
                //TODO: fix this method so that it recreates a new recycler view when the user wants to refresh
                //updating recycler view glitching, sending wrong days
                //recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        /**
         * This onclickListener is used to toggle notifications on or off. When toggling notifications on,
         * it iterates through the list of calendar events and sets a new alarm for each one. When toggling
         * them off, it calls the cancelAllAlarms method.
         */
        buttonToggleNotifications.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (notificationsActive) {
                    notificationsActive = false;
                    savePreference(false);

                    //all alarms are cancelled when the notifications are toggled off
                    cancelAllAlarms();

                    notificationsOnOrOff.setText("Notifications are OFF");
                } else {
                    notificationsActive = true;
                    savePreference(true);

                    //when the nofifications are toggled on, we must iterate through the class schedule and set a new alarm for each event
                    for (CalendarEvent c : schedule.getEvents()) {
                        Date date = c.getStartDate();
                        int hour = date.getHours();
                        int minute = date.getMinutes();
                        int day;

                        if (c.getDays() != null) {
                            if (c.getDays().get("Sunday")) {
                                day = Calendar.SUNDAY;
                                startAlarm(day, hour, minute);
                            }
                            if (c.getDays().get("Monday")) {
                                day = Calendar.MONDAY;
                                startAlarm(day, hour, minute);
                            }
                            if (c.getDays().get("Tuesday")) {
                                day = Calendar.TUESDAY;
                                startAlarm(day, hour, minute);
                            }
                            if (c.getDays().get("Wednesday")) {
                                day = Calendar.WEDNESDAY;
                                startAlarm(day, hour, minute);
                            }
                            if (c.getDays().get("Thursday")) {
                                day = Calendar.THURSDAY;
                                startAlarm(day, hour, minute);
                            }
                            if (c.getDays().get("Friday")) {
                                day = Calendar.FRIDAY;
                                startAlarm(day, hour, minute);
                            }
                            if (c.getDays().get("Saturday")) {
                                day = Calendar.SATURDAY;
                                startAlarm(day, hour, minute);
                            }
                        } else {
                            System.out.println("CalendarEvent is null");
                        }

                    }

                    notificationsOnOrOff.setText("Notifications are ON");
                }

            }
        });

        //create a list of events that must be displayed, which will be used by the recyclerview
        ArrayList<CalendarEventDisplayCard> eventsToDisplay = new ArrayList<>();
        for (CalendarEvent ce : schedule.getEvents()) {
            eventsToDisplay.add(new CalendarEventDisplayCard(R.drawable.ic_event_black_24dp, ce.getTitle(), ce.getLocation(), (HashMap<String, Boolean>) ce.getDays()));
        }

        //set up the recyclerview
        recyclerView = findViewById(R.id.classScheduleRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new CalendarEventDisplayAdapter(eventsToDisplay);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

    }

    /**
     * This method reads calendar events - it uses the cursor to get values from each column to be assembled
     * and displayed to the user. Events are filtered for classes that follow a standard naming format, containing
     * 3 numbers and a concordia class code, or the word "lecture" or "tutorial"
     *
     * @throws NullPointerException
     */
    public void readEvents() throws NullPointerException {
        while (cursor != null && cursor.moveToNext()) {
            //get the column ids of the calendar attributes
            int id1 = cursor.getColumnIndex(BaseColumns._ID);
            int id2 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
            int id3 = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
            int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
            int id5 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
            int id6 = cursor.getColumnIndex(CalendarContract.Events.DTEND);
            int id7 = cursor.getColumnIndex(CalendarContract.Events.RRULE);
            int id8 = cursor.getColumnIndex(CalendarContract.Events.DURATION);
            int id9 = cursor.getColumnIndex(CalendarContract.Events.RDATE);

            //get values associated with those column ids
            String idValue = cursor.getString(id1);
            int idInt = cursor.getInt(id1);
            String titleValue = cursor.getString(id2);
            String descriptionValue = cursor.getString(id3);
            String locationValue = cursor.getString(id4);
            long startTime = cursor.getLong(id5);
            long endTime = cursor.getLong(id6);
            String repetitionRule = cursor.getString(id7);
            String duration = cursor.getString(id8);
            String rDate = cursor.getString(id9);

            //if the event has a title, check to make sure it is a valid lecture or tutorial
            if (titleValue != null) {
                System.out.println(idValue + " - " + titleValue + " - " + new Date(startTime).toString());

                //regex for matching event title patterns - looking for no more than 3 digits
                Pattern threeDigitPattern = Pattern.compile("\\d{3}");
                Pattern moreThanThreeDigitPattern = Pattern.compile("\\d{4,100}");

                Matcher threeDigitMatcher = threeDigitPattern.matcher(titleValue);
                Matcher moreThanThreeDigitMatcher = moreThanThreeDigitPattern.matcher(titleValue);

                //if matches 3 digits and does not match more than 3 digits, it is likely a class with a 3 digit number
                //if it contains a valid class name
                boolean hasClassName = false;
                for (String s : ClassSchedule.getValidClasses()) {
                    if (titleValue.toLowerCase().contains(s)) hasClassName = true;
                }

                if (new Date(startTime).after(new Date(ClassSchedule.getImportantDates().get("winter2020start"))) && hasClassName && threeDigitMatcher.find() && !moreThanThreeDigitMatcher.find()) {
                    schedule.addEvent(new CalendarEvent(idInt, idValue, titleValue, locationValue, startTime, endTime, repetitionRule));
                }
            }
        }
        eventsRead = "eventsRead";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startAlarm(int day, int hours, int minutes) {
        //automatically set alarm id after other alarms that are in the system
        int alarmId;
        if (activeAlarmIds.isEmpty()) alarmId = 0;
        else alarmId = activeAlarmIds.get(activeAlarmIds.size() - 1) + 1;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, day);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        activeAlarmIds.add(alarmId);
        System.out.println("Alarm has been set for day " + day + " at " + hours + ":" + minutes);
        System.out.println("id: " + alarmId + ", size: " + activeAlarmIds.size());
    }

    /**
     * Uses the AlarmManager to clear all active alarms.
     * Clears the active alarm IDs from activeAlarmIds.
     */
    public void cancelAllAlarms() {
        for (int i = 0; i < activeAlarmIds.size(); i++) {
            int alarmId = activeAlarmIds.get(i);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

            alarmManager.cancel(pendingIntent);
        }
        activeAlarmIds.clear();

        System.out.println("All alarms cancelled");
    }

    /**
     * This method uses android's SharedPreferences to save the user's preference for whether notifications are active or not.
     *
     * @param active - boolean, whether the user wants notifications to be active.
     */
    public void savePreference(boolean active) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(NOTIFICATIONS_ACTIVE, active);
        editor.apply();

        updateViewsForNotificationPreference();
    }

    /**
     * Loads the user's saved preference for whether notifications should be active ore not.
     */
    public void loadPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        notificationsActive = sharedPreferences.getBoolean(NOTIFICATIONS_ACTIVE, false);

        updateViewsForNotificationPreference();
    }

    /**
     * Updates the icon for the alarm toggle button and the textview specifying whether notifications are active.
     */
    public void updateViewsForNotificationPreference() {
        TextView tv = findViewById(R.id.textViewNotificationsOnOrOff);
        FloatingActionButton b = findViewById(R.id.buttonToggleNotifications);

        if (notificationsActive) {
            tv.setText("Notifications are ON");
            b.setImageResource(R.drawable.ic_alarm_on_black_24dp);
            b.setTag(R.drawable.ic_alarm_on_black_24dp);
        } else {
            tv.setText("Notifications are OFF");
            b.setImageResource(R.drawable.ic_alarm_off_black_24dp);
            b.setTag(R.drawable.ic_alarm_off_black_24dp);
        }
    }

}

