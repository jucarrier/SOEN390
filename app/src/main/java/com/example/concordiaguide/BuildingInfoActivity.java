package com.example.concordiaguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import Helpers.ObjectWrapperForBinder;
import Models.Building;

public class BuildingInfoActivity extends AppCompatActivity {
    private static final String TAG = "BuildingInfoActivity";
    Building building;
    private Button directions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info);
        Toolbar toolbar = findViewById(R.id.building_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set info
        Log.d(TAG, "onClickEvent: caught");
        building = (Building) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("building")).getData();

        TextView name = findViewById(R.id.building_info_name);
        TextView address = findViewById(R.id.building_info_address);
        TextView description = findViewById(R.id.building_info_description);

        name.setText(building.getName());
        address.setText(building.getAddress());
        description.setText(building.getDescription());

        directions = findViewById(R.id.directions_button);

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionsButtonClicked(building);
            }
        });
    }

    private void directionsButtonClicked(Building b) {
        final Bundle bundle = new Bundle();
        bundle.putBinder("building", new ObjectWrapperForBinder(b));
        Intent openMainActivity = new Intent(this, MainActivity.class).putExtras(bundle);
        /*
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityIfNeeded(openMainActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        */
        this.startActivity(openMainActivity);
    }
}
