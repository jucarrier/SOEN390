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
import Models.Building;

public class Shuttle extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);
        final LatLng[] latlng = new LatLng[2];

        //Size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));


        //Cardview from choosing go Loyola or go SGW
        final Button goCampusClose = (Button) findViewById(R.id.shuttle_goCampusClose);

        Button goLoyola = (Button) findViewById(R.id.shuttle_goLoyola);
        goLoyola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final CardView goCampus = (CardView) findViewById(R.id.shuttle_goCampus);
                campuText("Direction to Loyola", "\nPlease go to the GREEN Marker (1455 Boulevard de Maisonneuve O) using your preferred travel method and see schedule for the next Shuttle departure");
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

        Button goSGW = (Button) findViewById(R.id.shuttle_goSGW);
        goSGW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final CardView goCampus = (CardView) findViewById(R.id.shuttle_goCampus);
                campuText("Direction to SGW", "\nPlease go to GREEN Marker (7137 Sherbrooke St. W., Loyola Campus) using your preferred travel method and see schedule for the next Shuttle departure");
                goCampus.setVisibility(View.VISIBLE);
                goCampusClose.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View pw) {
                        Shuttle.super.onBackPressed();
                    }
                });
                latlng[0] = new LatLng(45.458372, -73.638267);
                latlng[1] = new LatLng(45.497041, -73.578481);
            }
        });

        Button start = (Button) findViewById(R.id.shuttle_goCampusStart);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                Shuttle.super.onBackPressed();
                goToCampus(latlng[0], latlng[1]);
            }
        });

        Button goCampusSchedule = (Button) findViewById(R.id.shuttle_goCampusSeeSchedule);
        goCampusSchedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                startActivity(new Intent(getApplicationContext(), Shuttle_schedule.class));
            }
        });


        //set listener for buttons
        Button buttonClose = (Button) findViewById(R.id.shuttle_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                Shuttle.super.onBackPressed();
            }
        });

        Button buttonInfo = (Button) findViewById(R.id.shuttle_schedule);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                startActivity(new Intent(getApplicationContext(), Shuttle_schedule.class));
            }
        });
    }

    public void campuText(String title, String desc) {
        TextView campTitle = (TextView) findViewById(R.id.shuttle_goCampusTitle);
        TextView campDesc = (TextView) findViewById(R.id.shuttle_goCampusDesc);
        campTitle.setText(title);
        campDesc.setText(desc);
    }

    String shuttle_from, shuttle_to;
    private void goToCampus(LatLng from, LatLng to) {
        final Bundle bundle = new Bundle();
        bundle.putBinder("From", new ObjectWrapperForBinder(from));
        bundle.putBinder("To", new ObjectWrapperForBinder(to));
        bundle.putBoolean("active", true);
        this.startActivity(new Intent(this, MainActivity.class).putExtras(bundle));
    }
}