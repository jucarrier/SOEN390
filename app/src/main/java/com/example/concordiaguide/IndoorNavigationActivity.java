package com.example.concordiaguide;

import android.graphics.Color;
import android.os.Bundle;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.Objects;

import Helpers.ObjectWrapperForBinder;
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
        setContentView(R.layout.activity_indoor_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.floor_view);
        sgw = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("sgw")).getData();
        layola = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("loyola")).getData();
        selectedCampus = sgw;

        Spinner campusSpinner = (Spinner) findViewById(R.id.campus_spinner);
//        ArrayAdapter<String>

        try {
            highlightRoom("H867");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
