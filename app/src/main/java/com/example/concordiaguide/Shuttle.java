package com.example.concordiaguide;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class Shuttle extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));

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
    }
}
