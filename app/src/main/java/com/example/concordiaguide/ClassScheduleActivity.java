package com.example.concordiaguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button buttonShowEvents = (Button) findViewById(R.id.buttonShowCalendarEvents);
        final TextView showCalendarEvents = (TextView) findViewById(R.id.textViewShowCalendarEvents);   //this needs to be final because it is accessed by an inner class
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

        buttonShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cursor.equals(null)) Toast.makeText(getApplicationContext(), "cursor is null", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "cursor exists", Toast.LENGTH_LONG).show();

                while(cursor.moveToNext()){
                    if(cursor!=null){
                        int id1 = cursor.getColumnIndex(CalendarContract.Events._ID);
                        int id2 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                        int id3 = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                        int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                        int id5 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                        int id6 = cursor.getColumnIndex(CalendarContract.Events.DTEND);

                        String idValue = cursor.getString(id1);
                        String titleValue = cursor.getString(id2);
                        String descriptionValue = cursor.getString(id3);
                        String locationValue = cursor.getString(id4);
                        int startTime = cursor.getInt(id5); //start time in ms since 1970
                        int endTime = cursor.getInt(id6);   //this time is in milliseconds since 1970

                        schedule.addEvent(new CalendarEvent(idValue, titleValue, locationValue, startTime, endTime));

                        Date startAsDate = new java.util.Date((startTime*1000));


                        if (titleValue!=null){

                            //System.out.println(titleValue);

                            Pattern p = Pattern.compile("\\d{3}\\D");
                            Matcher m = p.matcher(titleValue);

                            if(m.find()) System.out.println("matches " + titleValue + ", starttime: " + startAsDate.toString());
                        }



                    }else{

                        showCalendarEvents.setText("no events found");

                    }
                }

            }
        });

    }

}
