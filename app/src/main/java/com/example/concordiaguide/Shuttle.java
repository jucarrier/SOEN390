package com.example.concordiaguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.model.LatLng;

import Helpers.ObjectWrapperForBinder;

public class Shuttle extends Activity {
    final LatLng[] latlng = new LatLng[2];
    /**
     * Called from the onClick button 'start' in onCreate.
     * It will assign the specific GPS location of the desired campus
     *
     * @param from the
     * @param to
     */
    final Bundle bundle = new Bundle();
    String shuttle_from, shuttle_to;

    /**
     * Create a new activity that will show the options and directions of the shuttle
     *
     * @param savedInstanceState get the bundle of the stated of saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);

        //Size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));


        //Cardview from choosing go Loyola or go SGW
        final Button goCampusClose = findViewById(R.id.shuttle_goCampusClose);

        Button goLoyola = findViewById(R.id.shuttle_goLoyola);
        goLoyola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final CardView goCampus = findViewById(R.id.shuttle_goCampus);
                campusText("Direction to Loyola", "\nPlease go to the GREEN Marker (1455 Boulevard de Maisonneuve O) using your preferred travel method and see schedule for the next Shuttle departure");
                goCampus.setVisibility(View.VISIBLE);
                goCampusClose.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View pw) {
                        Shuttle.super.onBackPressed();
                    }
                });
                latlng[0] = new LatLng(45.497041, -73.578481);
                latlng[1] = new LatLng(45.458372, -73.638267);
            }
        });

        Button goSGW = findViewById(R.id.shuttle_goSGW);
        goSGW.setOnClickListener(pw -> {
            final CardView goCampus = findViewById(R.id.shuttle_goCampus);
            campusText("Direction to SGW", "\nPlease go to GREEN Marker (7137 Sherbrooke St. W., Loyola Campus) using your preferred travel method and see schedule for the next Shuttle departure");
            goCampus.setVisibility(View.VISIBLE);
            goCampusClose.setOnClickListener(pw12 -> Shuttle.super.onBackPressed());
            latlng[0] = new LatLng(45.458372, -73.638267);
            latlng[1] = new LatLng(45.497041, -73.578481);
        });

        Button start = findViewById(R.id.shuttle_goCampusStart);
        start.setOnClickListener(pw -> {
            Shuttle.super.onBackPressed();
            goToCampus(latlng[0], latlng[1]);
        });

        Button goCampusSchedule = findViewById(R.id.shuttle_goCampusSeeSchedule);
        goCampusSchedule.setOnClickListener(pw -> startActivity(new Intent(getApplicationContext(), ShuttleSchedule.class)));

        //set listener for buttons
        Button buttonClose = findViewById(R.id.shuttle_close);
        buttonClose.setOnClickListener(pw -> Shuttle.super.onBackPressed());

        Button buttonInfo = findViewById(R.id.shuttle_schedule);
        buttonInfo.setOnClickListener(pw -> startActivity(new Intent(getApplicationContext(), ShuttleSchedule.class)));
    }

    /**
     * Called from the onClick button of SGW or Loyola in onCreate.
     * It will set the title and the description of the shuttle direction depending on the user's choice.
     *
     * @param title the title of the campus the user wish to go
     * @param desc  the description of the campus the user wish to go
     */
    public void campusText(String title, String desc) {
        TextView campTitle = findViewById(R.id.shuttle_goCampusTitle);
        TextView campDesc = findViewById(R.id.shuttle_goCampusDesc);
        campTitle.setText(title);
        campDesc.setText(desc);
    }

    public void goToCampus(LatLng from, LatLng to) {
        bundle.putBinder("From", new ObjectWrapperForBinder(from));
        bundle.putBinder("To", new ObjectWrapperForBinder(to));
        bundle.putBoolean("active", true);
        this.startActivity(new Intent(this, MainActivity.class).putExtras(bundle));
    }
}