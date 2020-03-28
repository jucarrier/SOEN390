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
    private Button directions;
    Building building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info);
        Toolbar toolbar = findViewById(R.id.building_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set info
        Log.d(TAG, "onClickEvent: caught");
        //Building building = (Building) ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("building")).getData();
        building = (Building) ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("building")).getData();

        TextView name = findViewById(R.id.building_info_name);
        TextView address = findViewById(R.id.building_info_address);
        TextView description = findViewById(R.id.building_info_description);

        name.setText(building.getName());
        address.setText(building.getAddress());
        description.setText(building.getDescription());

        directions = (Button) findViewById(R.id.directions_button);

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionsButtonClicked(building);
            }
        });
    }

    private void directionsButtonClicked(Building b){
        final Bundle bundle = new Bundle();
        bundle.putBinder("building", new ObjectWrapperForBinder(b));
        this.startActivity(new Intent(this, MainActivity.class).putExtras(bundle));
    }
}
