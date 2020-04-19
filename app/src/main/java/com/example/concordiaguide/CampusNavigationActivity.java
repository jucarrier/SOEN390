package com.example.concordiaguide;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Helpers.BuildingListAdapter;
import Helpers.ObjectWrapperForBinder;
import Models.Campus;

public class CampusNavigationActivity extends AppCompatActivity {

    private Campus sgw, loyola;
    private Campuses currentCampus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set event listener for switch button
        Switch switchButton = findViewById(R.id.campus_switch);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                TextView title = findViewById(R.id.building_list_campus);
                RecyclerView recyclerViewSgw = findViewById(R.id.building_list_sgw);
                RecyclerView recyclerViewLoyola = findViewById(R.id.building_list_loyola);

                if (b == false) {
                    title.setText("SGW");
                    currentCampus = Campuses.SGW;
                    recyclerViewSgw.setVisibility(View.VISIBLE);
                    recyclerViewLoyola.setVisibility(View.GONE);
                } else {
                    title.setText("Loyola");
                    currentCampus = Campuses.LAYOLA;
                    recyclerViewSgw.setVisibility(View.GONE);
                    recyclerViewLoyola.setVisibility(View.VISIBLE);
                }
            }
        });

        //get bundle sent
        if (sgw == null) {
            sgw = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("sgw")).getData();
        }
        if (loyola == null) {
            loyola = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("loyola")).getData();
        }

        //create list for sgw and loyola
        RecyclerView recyclerViewSgw = findViewById(R.id.building_list_sgw);
        recyclerViewSgw.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapterSgw = new BuildingListAdapter(this, sgw.getBuildings());
        recyclerViewSgw.setAdapter(mAdapterSgw);

        RecyclerView recyclerViewLoyola = findViewById(R.id.building_list_loyola);
        recyclerViewLoyola.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapterLoyola = new BuildingListAdapter(this, loyola.getBuildings());
        recyclerViewLoyola.setAdapter(mAdapterLoyola);

        //set title
        TextView title = findViewById(R.id.building_list_campus);
        if (currentCampus == null) {
            title.setText("SGW");
            currentCampus = Campuses.SGW;
            recyclerViewSgw.setVisibility(View.VISIBLE);
            recyclerViewLoyola.setVisibility(View.GONE);
        } else {
            if (currentCampus == Campuses.SGW) {
                title.setText("SGW");
                currentCampus = Campuses.SGW;
                recyclerViewSgw.setVisibility(View.VISIBLE);
                recyclerViewLoyola.setVisibility(View.GONE);
            } else {
                title.setText("Loyola");
                currentCampus = Campuses.LAYOLA;
                recyclerViewSgw.setVisibility(View.GONE);
                recyclerViewLoyola.setVisibility(View.VISIBLE);
                switchButton.setChecked(true);
            }
        }

    }

    enum Campuses {
        SGW,
        LAYOLA
    }

}
