package com.example.concordiaguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class Shuttle extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);

        //Size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));


        //Cardview from choosing go Loyola or go SGW
        Button goLoyola = (Button)findViewById(R.id.shuttle_goLoyola);
        goLoyola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final CardView goCampus = (CardView) findViewById(R.id.shuttle_goCampus) ;
                campuText("Direction to Loyola", "\nPlease go to SGW Shuttle stop (1455 Boulevard de Maisonneuve) O using your preferred travel method and see schedule for the next Shuttle departure");
                goCampus.setVisibility(View.VISIBLE);
            }
        });

        Button goSGW = (Button)findViewById(R.id.shuttle_goSGW);
        goSGW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final CardView goCampus = (CardView) findViewById(R.id.shuttle_goCampus) ;
                campuText("Direction to SGW", "\nPlease go to Loyola Shuttle stop (7137 Sherbrooke St. W., Loyola Campus) using your preferred travel method and see schedule for the next Shuttle departure");
                goCampus.setVisibility(View.VISIBLE);
            }
        });

        Button goCampusClose = (Button)findViewById(R.id.shuttle_goCampusClose);
        goCampusClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                Shuttle.super.onBackPressed();
            }
        });

        Button goCampusSchedule = (Button)findViewById(R.id.shuttle_goCampusSeeSchedule);
        goCampusSchedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                startActivity(new Intent(getApplicationContext(), Shuttle_schedule.class));
            }
        });



        //set listener for buttons
        Button buttonClose = (Button)findViewById(R.id.shuttle_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                Shuttle.super.onBackPressed();
            }
        });

        Button buttonInfo = (Button)findViewById(R.id.shuttle_schedule);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                startActivity(new Intent(getApplicationContext(), Shuttle_schedule.class));
            }
        });
        }

    public void campuText(String title, String desc){
        TextView campTitle = (TextView) findViewById(R.id.shuttle_goCampusTitle);
        TextView campDesc = (TextView) findViewById(R.id.shuttle_goCampusDesc);
        campTitle.setText(title);
        campDesc.setText(desc);
    }
}
