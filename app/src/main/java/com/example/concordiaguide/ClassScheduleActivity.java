package com.example.concordiaguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import Helpers.ClassSchedule;
import Models.CalendarEvent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassScheduleActivity extends AppCompatActivity {
    //TODO: make this a recyclerView so we can scroll and see the events

    Cursor cursor;
    ClassSchedule schedule = new ClassSchedule(new ArrayList<CalendarEvent>()); //create an empty schedule to work with

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button buttonFindCalendarEvents = (Button) findViewById(R.id.buttonFindCalendarEvents);
        Button buttonShowCalendarEvents = (Button) findViewById(R.id.buttonShowCalendarEvents);
        final TextView showCalendarEvents = (TextView) findViewById(R.id.textViewShowCalendarEvents);
        //showCalendarEvents.setText("default text here");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
            return;
        }

        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);

        buttonFindCalendarEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //uncomment this to have a message that shows whether the cursor is null or not
//                if(cursor.equals(null)) Toast.makeText(getApplicationContext(), "cursor is null", Toast.LENGTH_LONG).show();
//                else Toast.makeText(getApplicationContext(), "cursor exists", Toast.LENGTH_LONG).show();

                while(cursor.moveToNext()){
                    if(cursor!=null){
                        int id1 = cursor.getColumnIndex(CalendarContract.Events._ID);
                        int id2 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                        int id3 = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                        int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                        int id5 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                        int id6 = cursor.getColumnIndex(CalendarContract.Events.DTEND);
                        int id7 = cursor.getColumnIndex(CalendarContract.Events.RRULE);
                        int id8 = cursor.getColumnIndex(CalendarContract.Events.DURATION);
                        int id9 = cursor.getColumnIndex(CalendarContract.Events.RDATE);

                        String idValue = cursor.getString(id1);
                        int idInt = cursor.getInt(id1);
                        String titleValue = cursor.getString(id2);
                        String descriptionValue = cursor.getString(id3);
                        String locationValue = cursor.getString(id4);
                        long startTime = (long) cursor.getLong(id5); //start time in ms since 1970 and add 1 more month
                        long endTime = (long) cursor.getLong(id6);   //this time is in milliseconds since 1970 and add 1 more month
                        String repetitionRule = cursor.getString(id7);
                        String duration = cursor.getString(id8);
                        String rDate = cursor.getString(id9);


                        java.util.Date time = new java.util.Date(startTime);
                        System.out.println(time + " <- time");

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
                                //System.out.println("new event added, id: " + idValue + ", title: " + titleValue + ", oStart: " + new Date(originalStart).toString());
                            }
                        }

//                        if (titleValue!=null){
//
//                            //System.out.println(titleValue);
//
//                            Pattern p = Pattern.compile("\\d{3}\\D");
//                            Matcher m = p.matcher(titleValue);
//
//                            //uncomment this to see which events are being found
//                            if(m.find()) System.out.println("matches " + titleValue + ", starttime: " + startAsDate.toString() + ", repRule " + repetitionRule);
//                        }



                    }else{

                        System.out.println("no events found");

                    }
                }

            }
        });

        //method to show the saved events that were retrieved from the calendar
        buttonShowCalendarEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToDisplay = "";
                for(CalendarEvent event : schedule.getEvents()){
                    if (event.getTitle() != null){

                        //uncomment this to see which events are being shown
                        System.out.println(event.getId() + " - " + event.getTitle() + ", dtStart: " + event.getDtStart() + ", rRule: " + event.getrRule());

                        //regex for matching event title patterns - looking for no more than 3 digits
                        Pattern threeDigitPattern = Pattern.compile("\\d{3}");
                        Pattern moreThanThreeDigitPattern = Pattern.compile("\\d{4,100}");

                        Matcher threeDigitMatcher = threeDigitPattern.matcher(event.getTitle());
                        Matcher moreThanThreeDigitMatcher = moreThanThreeDigitPattern.matcher(event.getTitle());

                        //if matches 3 digits and does not match more than 3 digits, it is likely a class with a 3 digit number
                        //if it contains a valid class name
                        boolean hasClassName = false;
                        for(String s : ClassSchedule.getValidClasses()){
                            if(event.getTitle().toLowerCase().contains(s)) hasClassName = true;
                        }

                        if(new Date(event.getDtStart()).after(new Date(ClassSchedule.getImportantDates().get("winter2020start"))) && hasClassName && threeDigitMatcher.find() && !moreThanThreeDigitMatcher.find()){
                            System.out.println(event.getId() + " - " + event.getTitle() + " <- approved for display");
                            textToDisplay = textToDisplay + event.getId() + " - " + event.getTitle();
                            try {
                                if(event.getDays().get("Sunday") == Boolean.TRUE) textToDisplay = textToDisplay + " Sunday ";
                                if(event.getDays().get("Monday") == Boolean.TRUE) textToDisplay = textToDisplay + " Monday ";
                                if(event.getDays().get("Tuesday") == Boolean.TRUE) textToDisplay = textToDisplay + " Tuesday ";
                                if(event.getDays().get("Wednesday") == Boolean.TRUE) textToDisplay = textToDisplay + " Wednesday ";
                                if(event.getDays().get("Thursday") == Boolean.TRUE) textToDisplay = textToDisplay + " Thursday ";
                                if(event.getDays().get("Friday") == Boolean.TRUE) textToDisplay = textToDisplay + " Friday ";
                                if(event.getDays().get("Saturday") == Boolean.TRUE) textToDisplay = textToDisplay + " Saturday ";
                            } catch (Exception e){
                                System.out.println(e.toString());
                            }

                            textToDisplay = textToDisplay + "\n";
                        }



//                        if(event.getDays() == null) System.out.println("days is null");
//                        else System.out.println(event.getDays().keySet() + " <- monday");




                    } else{
                        textToDisplay = "There are no events in the calendar - add some\n";
                    }



                }

                showCalendarEvents.setText(textToDisplay);
            }
        });



    }

}
