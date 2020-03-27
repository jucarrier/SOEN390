package com.example.concordiaguide;

import android.app.Activity;
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
        final String[] campus = new String[1];

        //Size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));

        Button goLoyola = (Button)findViewById(R.id.shuttle_goLoyola);
        goLoyola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final CardView goCampus = (CardView) findViewById(R.id.shuttle_goCampus) ;
                campus[0] = "Direction to Loyola";
                if (goCampus.getVisibility()== View.VISIBLE)
                    goCampus.setVisibility(View.INVISIBLE);
                else if (goCampus.getVisibility()== View.INVISIBLE)
                    goCampus.setVisibility(View.VISIBLE);
            }
        });

        Button goCampusClose = (Button)findViewById(R.id.shuttle_goCampusClose);
        goCampusClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                Shuttle.super.onBackPressed();
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
            }
        });

        TextView campTitle = (TextView)findViewById(R.id.shuttle_goCampusDesc);
        campTitle.setText(campus[0]);
    }

}
