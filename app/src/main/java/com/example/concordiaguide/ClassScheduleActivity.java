package com.example.concordiaguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassScheduleActivity extends AppCompatActivity {
    Cursor cursor;

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
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

                        String idValue = cursor.getString(id1);
                        String titleValue = cursor.getString(id2);
                        String descriptionValue = cursor.getString(id3);
                        String locationValue = cursor.getString(id4);

                        if (titleValue!=null){

                            //System.out.println(titleValue);

                            Pattern p = Pattern.compile("\\d{3}\\D");
                            Matcher m = p.matcher(titleValue);

                            if(m.find()) System.out.println("matches " + titleValue);
                        }



                    }else{

                        showCalendarEvents.setText("no events found");

                    }
                }

            }
        });

    }

}
