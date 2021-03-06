package com.example.concordiaguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Helpers.ObjectWrapperForBinder;
import Models.Building;

public class BuildingInfoPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Building building = (Building) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("building")).getData();

        setContentView(R.layout.popup_building_info);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.35));

        //set listener for buttons
        Button buttonClose = findViewById(R.id.popup_button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                BuildingInfoPopup.super.onBackPressed();
            }
        });

        Button buttonInfo = findViewById(R.id.popup_button_info);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pw) {
                final Bundle bundle = new Bundle();
                bundle.putBinder("building", new ObjectWrapperForBinder(building));
                BuildingInfoPopup.this.startActivity(new Intent(BuildingInfoPopup.this, BuildingInfoActivity.class).putExtras(bundle));
            }
        });

        //get views
        TextView name = findViewById(R.id.popup_name);
        TextView address = findViewById(R.id.popup_address);

        //set view content
        name.setText(building.getName());
        address.setText(building.getAddress());
    }
}
