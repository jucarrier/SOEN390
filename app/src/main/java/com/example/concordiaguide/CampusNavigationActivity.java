package com.example.concordiaguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import Helpers.BuildingListAdapter;
import Helpers.ObjectWrapperForBinder;
import Models.Campus;

public class CampusNavigationActivity extends AppCompatActivity {

    enum Campuses {
        SGW,
        Loyola
    }

    private Campus sgw, loyola;
    private Campuses currentCampus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set event listener for switch button
        Switch switchButton = (Switch) findViewById(R.id.campus_switch);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                TextView title = findViewById(R.id.building_list_campus);
                RecyclerView recyclerViewSgw = (RecyclerView) findViewById(R.id.building_list_sgw);
                RecyclerView recyclerViewLoyola = (RecyclerView) findViewById(R.id.building_list_loyola);

                if(b == false) {
                    title.setText("SGW");
                    currentCampus = Campuses.SGW;
                    recyclerViewSgw.setVisibility(View.VISIBLE);
                    recyclerViewLoyola.setVisibility(View.GONE);
                } else {
                    title.setText("Loyola");
                    currentCampus = Campuses.Loyola;
                    recyclerViewSgw.setVisibility(View.GONE);
                    recyclerViewLoyola.setVisibility(View.VISIBLE);
                }
            }
        });

        //set title
        TextView title = findViewById(R.id.building_list_campus);
        title.setText("SGW");
        currentCampus = Campuses.SGW;

        //get bundle sent
        sgw = (Campus) ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("sgw")).getData();
        loyola = (Campus) ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("loyola")).getData();

        //create list for sgw and loyola
        RecyclerView recyclerViewSgw = (RecyclerView) findViewById(R.id.building_list_sgw);
        recyclerViewSgw.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapterSgw = new BuildingListAdapter(this, sgw.getBuildings());
        recyclerViewSgw.setAdapter(mAdapterSgw);

        RecyclerView recyclerViewLoyola = (RecyclerView) findViewById(R.id.building_list_loyola);
        recyclerViewLoyola.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapterLoyola = new BuildingListAdapter(this, loyola.getBuildings());
        recyclerViewLoyola.setAdapter(mAdapterLoyola);
        recyclerViewLoyola.setVisibility(View.GONE);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

    }

}
