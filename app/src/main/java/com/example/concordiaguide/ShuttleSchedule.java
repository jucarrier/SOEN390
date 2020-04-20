package com.example.concordiaguide;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class ShuttleSchedule extends Activity {
    /**
     * This will display the schedule of the shuttle in the Shuttle display
     *
     * @param savedInstanceState get the bundle of the stated of saved instance
     */

    Boolean test = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle_schedule);

        //Size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width * 1, (int) (height * 0.6));
        test = true;
        //set listener for buttons
        Button buttonClose = findViewById(R.id.shuttle_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                ShuttleSchedule.super.onBackPressed();
            }
        });
    }
}
