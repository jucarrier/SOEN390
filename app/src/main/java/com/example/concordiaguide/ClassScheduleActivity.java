package com.example.concordiaguide;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import Helpers.CalendarEventDisplayAdapter;
import Helpers.ClassSchedule;
import Models.CalendarEvent;
import Models.CalendarEventDisplayCard;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassScheduleActivity extends AppCompatActivity {

    public ArrayList<Integer> activeAlarmIds = new ArrayList<>();

    Cursor cursor;
    ClassSchedule schedule = new ClassSchedule(new ArrayList<CalendarEvent>()); //create an empty schedule to work with
    public boolean notificationsActive = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton buttonToggleNotifications = (FloatingActionButton) findViewById(R.id.buttonToggleNotifications);
        final TextView notificationsOnOrOff = (TextView) findViewById(R.id.textViewNotificationsOnOrOff);

        if (!notificationsActive) buttonToggleNotifications.setImageResource(R.drawable.ic_alarm_off_black_24dp);

        //permission check to read calendar events
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
            return;
        }

        //cursor that reads the calendar events
        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);

        //read the events in the calendar as soon as the page opens
        readEvents();

        //toggle notifications on or off
        buttonToggleNotifications.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(notificationsActive){
                    notificationsActive=false;
                    buttonToggleNotifications.setImageResource(R.drawable.ic_alarm_off_black_24dp);
                    cancelAllAlarms();

                    notificationsOnOrOff.setText("Notifications are OFF");
                } else{
                    notificationsActive=true;
                    buttonToggleNotifications.setImageResource(R.drawable.ic_alarm_on_black_24dp);

                    for(CalendarEvent c : schedule.getEvents()){
                        Date date = c.getStartDate();
                        int hour = date.getHours();
                        int minute = date.getMinutes();
                        int day;

                        if(c.getDays()!=null){
                            if(c.getDays().get("Sunday")) {
                                day = Calendar.SUNDAY;
                                startAlarm(day, hour, minute);
                            }
                            if(c.getDays().get("Monday")) {
                                day = Calendar.MONDAY;
                                startAlarm(day, hour, minute);
                            }
                            if(c.getDays().get("Tuesday")) {
                                day = Calendar.TUESDAY;
                                startAlarm(day, hour, minute);
                            }
                            if(c.getDays().get("Wednesday")) {
                                day = Calendar.WEDNESDAY;
                                startAlarm(day, hour, minute);
                            }
                            if(c.getDays().get("Thursday")) {
                                day = Calendar.THURSDAY;
                                startAlarm(day, hour, minute);
                            }
                            if(c.getDays().get("Friday")) {
                                day = Calendar.FRIDAY;
                                startAlarm(day, hour, minute);
                            }
                            if(c.getDays().get("Saturday")) {
                                day = Calendar.SATURDAY;
                                startAlarm(day, hour, minute);
                            }
                        } else{
                            System.out.println("CalendarEvent is null");
                        }

                    }

                    notificationsOnOrOff.setText("Notifications are ON");
                }

            }
        });

        ArrayList<CalendarEventDisplayCard> eventsToDisplay = new ArrayList<>();
        for(CalendarEvent ce : schedule.getEvents()){
            eventsToDisplay.add(new CalendarEventDisplayCard(R.drawable.ic_event_black_24dp, ce.getTitle(), ce.getLocation(), (HashMap<String, Boolean>) ce.getDays()));
        }

        recyclerView = findViewById(R.id.classScheduleRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new CalendarEventDisplayAdapter(eventsToDisplay);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);
    }

    public void readEvents(){
        while(cursor.moveToNext()){
            if(cursor!=null){
                //get the column ids of the calendar attributes
                int id1 = cursor.getColumnIndex(CalendarContract.Events._ID);
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
                long startTime = (long) cursor.getLong(id5);
                long endTime = (long) cursor.getLong(id6);
                String repetitionRule = cursor.getString(id7);
                String duration = cursor.getString(id8);
                String rDate = cursor.getString(id9);

                //if the event has a title, check to make sure it is a valid lecture or tutorial
                if(titleValue!=null){
                    System.out.println(idValue + " - " + titleValue + " - "+ new Date(startTime).toString());

                    //regex for matching event title patterns - looking for no more than 3 digits
                    Pattern threeDigitPattern = Pattern.compile("\\d{3}");
                    Pattern moreThanThreeDigitPattern = Pattern.compile("\\d{4,100}");

                    Matcher threeDigitMatcher = threeDigitPattern.matcher(titleValue);
                    Matcher moreThanThreeDigitMatcher = moreThanThreeDigitPattern.matcher(titleValue);

                    //if matches 3 digits and does not match more than 3 digits, it is likely a class with a 3 digit number
                    //if it contains a valid class name
                    boolean hasClassName = false;
                    for(String s : ClassSchedule.getValidClasses()){
                        if(titleValue.toLowerCase().contains(s)) hasClassName = true;
                    }

                    if(new Date(startTime).after(new Date(ClassSchedule.getImportantDates().get("winter2020start"))) && hasClassName && threeDigitMatcher.find() && !moreThanThreeDigitMatcher.find()){
                        schedule.addEvent(new CalendarEvent(idInt, idValue, titleValue, locationValue, startTime, endTime, repetitionRule));
                    }
                }

            }else{
                System.out.println("no events found");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startAlarm(int day, int hours, int minutes){
        //automatically set alarm id after other alarms that are in the system
        int alarmId;
        if(activeAlarmIds.size()==0) alarmId = 0;
        else alarmId = activeAlarmIds.get(activeAlarmIds.size()-1);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, day);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        activeAlarmIds.add(alarmId);
        System.out.println("Alarm has been set for day "+day+" at " +hours + ":" + minutes);
    }

    private void cancelSpecificAlarm(int alarmId){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

        for(int i = 0; i<activeAlarmIds.size(); i++){
            if(activeAlarmIds.get(i) == alarmId) {
                activeAlarmIds.remove(i);
            }
        }

        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e){
            System.out.println(e.toString() + " <- if this is a null pointer exception, the alarm has likely already fired");
        }
        System.out.println("Alarm cancelled");
    }

    private void cancelAllAlarms(){
        for(int i = 0;i<activeAlarmIds.size(); i++){
            int alarmId = activeAlarmIds.get(i);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

            alarmManager.cancel(pendingIntent);
        }

        System.out.println("All alarms cancelled");
    }

}

