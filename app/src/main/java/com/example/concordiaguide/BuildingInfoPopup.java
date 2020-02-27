package com.example.concordiaguide;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.concordiaguide.tools.ObjectWrapperForBinder;

import Models.Building;

public class BuildingInfoPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Building building = (Building) ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("building")).getData();

        setContentView(R.layout.popup_building_info);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));

        //set listener for close button
        Button button = (Button)findViewById(R.id.popup_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                BuildingInfoPopup.super.onBackPressed();
            }
        });

        //get views
        TextView name = (TextView)findViewById(R.id.popup_name);
        TextView address = (TextView)findViewById(R.id.popup_address);
        TextView description = (TextView)findViewById(R.id.popup_description);

        //set view content
        name.setText(building.getName());
        address.setText(building.getAddress());
        description.setText(building.getDescription());
    }
}
