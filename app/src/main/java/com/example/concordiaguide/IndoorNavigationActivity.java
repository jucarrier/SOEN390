package com.example.concordiaguide;

import android.graphics.Color;
import android.os.Bundle;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import Helpers.ObjectWrapperForBinder;
import Models.Building;
import Models.Campus;
import Models.Floor;

public class IndoorNavigationActivity extends AppCompatActivity {
    private Campus selectedCampus;

    private Floor floor;
    private String[] floorLabels;

    private String room;
    private String building;
    private ImageView imageView;
    private Campus sgw;
    private Campus layola;
    private String[] campusLabels = {"SGW - Downtown", "Layola"};


    private void highlightRoom(String roomName) {
        VectorChildFinder vector = new VectorChildFinder(this, sgw.getBuilding("hall").getFloor(8).floorMap, imageView);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName(roomName);
        if (path1 != null) {
            path1.setFillColor(Color.BLUE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppCompatActivity self = this;

        setContentView(R.layout.activity_indoor_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.floor_view);
        sgw = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("sgw")).getData();
        layola = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("loyola")).getData();

        Spinner campusSpinner = (Spinner) findViewById(R.id.campus_spinner);
        ArrayAdapter<String> campusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campusLabels);
        campusSpinner.setAdapter(campusSpinnerAdapter);

        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner buildingSpinner = (Spinner) findViewById(R.id.building_spinner);
                ArrayList<String> buildingLabels = new ArrayList<>();
                final Campus selectedCampus = position == 0 ? sgw : layola;
                for (Building b : selectedCampus.getBuildings()) {
                    buildingLabels.add(b.getName());
                }
                ArrayAdapter<String> buildingSpinnerAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, buildingLabels.toArray(new String[0]));
                buildingSpinner.setAdapter(buildingSpinnerAdapter);

                buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner floorSpinner = (Spinner) findViewById(R.id.floor_spinner);
                        final Building selectedBuilding = selectedCampus.getBuildings().get(position);

                        ArrayList<String> floorLabels = new ArrayList<>();
                        for (Floor f : selectedBuilding.getFloors()) {
                            floorLabels.add(f.getFloorName());
                        }
                        ArrayAdapter<String> floorSpinnerAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, floorLabels.toArray(new String[0]));
                        floorSpinner.setAdapter(floorSpinnerAdapter);

                        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Floor selectedFloor = selectedBuilding.getFloors()[position];
                                ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<>(self, android.R.layout.simple_dropdown_item_1line, selectedFloor.getRoomNames());
                                final AutoCompleteTextView roomInput = (AutoCompleteTextView) findViewById(R.id.search_room_name);
                                roomInput.setAdapter(roomNameAdapter);

                                roomInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        highlightRoom(roomInput.getEditableText().toString());
                                        return true;
                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
