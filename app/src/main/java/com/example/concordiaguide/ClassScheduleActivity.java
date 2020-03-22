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

                        String idValue = cursor.getString(id1);
                        int idInt = cursor.getInt(id1);
                        String titleValue = cursor.getString(id2);
                        String descriptionValue = cursor.getString(id3);
                        String locationValue = cursor.getString(id4);
                        int startTime = cursor.getInt(id5); //start time in ms since 1970
                        int endTime = cursor.getInt(id6);   //this time is in milliseconds since 1970

                        schedule.addEvent(new CalendarEvent(idInt, idValue, titleValue, locationValue, startTime, endTime));

                        Date startAsDate = new java.util.Date((startTime*1000));


                        if (titleValue!=null){

                            //System.out.println(titleValue);

                            Pattern p = Pattern.compile("\\d{3}\\D");
                            Matcher m = p.matcher(titleValue);

                            if(m.find()) System.out.println("matches " + titleValue + ", starttime: " + startAsDate.toString());
                        }



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

                    System.out.println(event.getId() + " - " + event.getTitle());


                    if (event.getTitle() != null){
                        Pattern threeDigitPattern = Pattern.compile("\\d{3}");
                        Pattern moreThanThreeDigitPattern = Pattern.compile("\\d{4,100}");

                        Matcher threeDigitMatcher = threeDigitPattern.matcher(event.getTitle());
                        Matcher moreThanThreeDigitMatcher = moreThanThreeDigitPattern.matcher(event.getTitle());

                        //if matches 3 digits and does not match more than 3 digits, it is likely a class with a 3 digit number
                        if(threeDigitMatcher.find() && !moreThanThreeDigitMatcher.find()) textToDisplay = textToDisplay + event.getTitle() + "\n";
                    }



                }

                showCalendarEvents.setText(textToDisplay);
            }
        });



    }

}
