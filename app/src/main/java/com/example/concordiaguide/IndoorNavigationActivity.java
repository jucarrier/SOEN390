package com.example.concordiaguide;

import android.graphics.Color;
import android.os.Bundle;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Helpers.GraphBuilder;
import Helpers.ObjectWrapperForBinder;
import Models.Building;
import Models.Campus;
import Models.Edge;
import Models.Floor;
import Models.Node;

public class IndoorNavigationActivity extends AppCompatActivity {
    private ImageView imageView;
    private Campus sgw;
    private Campus layola;
    private String[] campusLabels = {"SGW - Downtown", "Layola"};
    private Spinner campusSpinner;
    private Spinner buildingSpinner;
    private Spinner floorSpinner;
    private AutoCompleteTextView roomInput;

    private Boolean isHandicapped = false;


    public VectorDrawableCompat.VFullPath highlightRoom(String roomName, int floorMap, Building building) {
        VectorChildFinder vector = new VectorChildFinder(this, floorMap, imageView);
        VectorDrawableCompat.VFullPath room = vector.findPathByName(roomName);
        if (room != null) {
            room.setFillColor(Color.BLUE);
            return room;
        }
        roomName = building.getInitials().toUpperCase() + roomName.replaceAll("\\D+", "");
        room = vector.findPathByName(roomName);
        if (room != null) {
            room.setFillColor(Color.BLUE);
        }
        return room;
    }

    public VectorDrawableCompat.VFullPath highlightPathToRoom(String roomNumber, Floor floor, boolean isHandicapped, Building building) {
        int floorMap = floor.getFloorMap();
        VectorDrawableCompat.VFullPath edge = null;

        if(roomNumber.matches("^[a-zA-Z]\\d+(?:\\.\\d+)?")) {
            roomNumber = roomNumber.substring(1);
        }

        GraphBuilder gb = new GraphBuilder(getResources().getXml(floorMap), floor);

        try {
            List<Edge> edges = gb.getShortestPathTo(roomNumber, isHandicapped, GraphBuilder.Direction.DOWN);

            VectorChildFinder vector = new VectorChildFinder(this, floorMap, imageView);

            for(Edge e : edges) {
                edge = vector.findPathByName(e.getEdgeName());
                if (edge != null) {
                    edge.setStrokeColor(Color.BLUE);
                }
            }

            roomNumber = building.getInitials() + roomNumber;
            roomNumber = roomNumber.toUpperCase();
            edge = vector.findPathByName(roomNumber);
            if (edge != null) {
                edge.setFillColor(Color.BLUE);
            }
        } catch (GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
        }

        return edge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppCompatActivity self = this;
        setContentView(R.layout.activity_indoor_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.floor_view);
        try {
            sgw = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("sgw")).getData();
            layola = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("loyola")).getData();
        } catch (Exception e) {
            return;
        }
        setUp(self);
    }

    public void setUp(final AppCompatActivity self) {
        campusSpinner = (Spinner) findViewById(R.id.campus_spinner);
        ArrayAdapter<String> campusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campusLabels);
        campusSpinner.setAdapter(campusSpinnerAdapter);

        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingSpinner = (Spinner) findViewById(R.id.building_spinner);
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
                        floorSpinner = (Spinner) findViewById(R.id.floor_spinner);
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
                                final Floor selectedFloor = selectedBuilding.getFloors()[position];
                                ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<>(self, android.R.layout.simple_dropdown_item_1line, selectedFloor.getRoomNames());
                                roomInput = (AutoCompleteTextView) findViewById(R.id.search_room_name);
                                roomInput.setAdapter(roomNameAdapter);
                                imageView.setImageResource(selectedFloor.getFloorMap());

                                roomInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        String room = roomInput.getEditableText().toString();
                                        //highlightRoom(room, selectedFloor.getFloorMap(), selectedBuilding);
                                        highlightPathToRoom(room, selectedFloor, isHandicapped, selectedBuilding);
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

    public void showDirections(Building sourceBuilding, Building targetBuilding, Floor sourceFloor, Floor targetFloor, String sourceRoom, String targetRoom, boolean isHandicapped) {
        int floorMap = sourceFloor.getFloorMap();
        GraphBuilder gb = new GraphBuilder(getResources().getXml(floorMap), sourceFloor);
        List<Edge> edges = null;
        VectorDrawableCompat.VFullPath edge = null;

        try {
            //is source and target are in the same building
            if(sourceBuilding.getName().equals(targetBuilding.getName())) {
                //if its on the same floor
                if(sourceFloor.getFloorLevel() == targetFloor.getFloorLevel()) {
                    //get edges to room
                    edges = gb.getShortestPath(sourceRoom, targetRoom);
                }

                //if its on a lower floor
                if(sourceFloor.getFloorLevel() > targetFloor.getFloorLevel()) {
                    //get edges to stairs/elevator doing down
                    edges = gb.getShortestPathFrom(sourceRoom, isHandicapped, GraphBuilder.Direction.DOWN);
                }

                //if its on a higher floor
                if(sourceFloor.getFloorLevel() < targetFloor.getFloorLevel()) {
                    //get edges to stairs/elevator doing up
                    edges = gb.getShortestPathFrom(sourceRoom, isHandicapped, GraphBuilder.Direction.UP);
                }

                //draw edges onto map
                VectorChildFinder vector = new VectorChildFinder(this, floorMap, imageView);

                for(Edge e : edges) {
                    edge = vector.findPathByName(e.getEdgeName());
                    if (edge != null) {
                        edge.setStrokeColor(Color.BLUE);
                    }
                }

                String roomNumber = sourceBuilding.getInitials() + sourceRoom;
                roomNumber = roomNumber.toUpperCase();
                edge = vector.findPathByName(roomNumber);
                if (edge != null) {
                    edge.setFillColor(Color.BLUE);
                }
            }
        } catch (GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
        }
    }

    public void setCampuses(Campus sgw, Campus layola) {
        this.sgw = sgw;
        this.layola = layola;
        setUp(this);
    }

    public Spinner getCampusSpinner() {
        return campusSpinner;
    }

    public Spinner getBuildingSpinner() {
        return buildingSpinner;
    }

    public Spinner getFloorSpinner() {
        return floorSpinner;
    }

    public AutoCompleteTextView getRoomInput() {
        return roomInput;
    }
}
