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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
    private Campus loyola;
    private String[] campusLabels = {"SGW - Downtown", "Loyola"};
    private Spinner campusSpinnerFrom;
    private Spinner buildingSpinnerFrom;
    private Spinner floorSpinnerFrom;
    private Spinner roomSpinnerFrom;
    private Spinner campusSpinnerTo;
    private Spinner buildingSpinnerTo;
    private Spinner floorSpinnerTo;
    private Spinner roomSpinnerTo;
    private AutoCompleteTextView roomInput;

    private Building sourceBuilding;
    private Floor sourceFloor;
    private String sourceRoom;
    private Building targetBuilding;
    private Floor targetFloor;
    private String targetRoom;

    private boolean isHandicapped = false;


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

    public VectorDrawableCompat.VFullPath highlightPathToRoom(String roomNumber, Floor floor, Building building) {
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
            loyola = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("loyola")).getData();
        } catch (Exception e) {
            return;
        }
        setUp(self);
    }

    public void setUp(final AppCompatActivity self) {
        campusSpinnerFrom = (Spinner) findViewById(R.id.campus_spinner_from);
        ArrayAdapter<String> campusSpinnerAdapterFrom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campusLabels);
        campusSpinnerFrom.setAdapter(campusSpinnerAdapterFrom);

        campusSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingSpinnerFrom = (Spinner) findViewById(R.id.building_spinner_from);
                ArrayList<String> buildingLabels = new ArrayList<>();
                final Campus selectedCampus = position == 0 ? sgw : loyola;
                for (Building b : selectedCampus.getBuildings()) {
                    buildingLabels.add(b.getName());
                }
                ArrayAdapter<String> buildingSpinnerAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, buildingLabels.toArray(new String[0]));
                buildingSpinnerFrom.setAdapter(buildingSpinnerAdapter);

                buildingSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        floorSpinnerFrom = (Spinner) findViewById(R.id.floor_spinner_from);
                        final Building selectedBuilding = selectedCampus.getBuildings().get(position);
                        sourceBuilding = selectedBuilding;

                        ArrayList<String> floorLabels = new ArrayList<>();

                        if(selectedBuilding.getFloors().length != 0) {
                            for (Floor f : selectedBuilding.getFloors()) {
                                floorLabels.add(f.getFloorName());
                            }
                            ArrayAdapter<String> floorSpinnerAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, floorLabels.toArray(new String[0]));
                            floorSpinnerFrom.setAdapter(floorSpinnerAdapter);
                        } else {
                            floorSpinnerFrom.setAdapter(null);

                            roomSpinnerFrom = (Spinner) findViewById(R.id.room_spinner_from);
                            roomSpinnerFrom.setAdapter(null);

                            sourceFloor = null;
                            sourceRoom = null;
                        }

                        floorSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                roomSpinnerFrom = (Spinner) findViewById(R.id.room_spinner_from);

                                final Floor selectedFloor = selectedBuilding.getFloors()[position];
                                sourceFloor = selectedFloor;

                                ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<>(self, android.R.layout.simple_dropdown_item_1line, selectedFloor.getRoomNames());
                                roomSpinnerFrom.setAdapter(roomNameAdapter);

                                imageView.setImageResource(selectedFloor.getFloorMap());

                                roomSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                        sourceRoom = selectedFloor.getRoomNames()[position];
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

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

        campusSpinnerTo = (Spinner) findViewById(R.id.campus_spinner_to);
        ArrayAdapter<String> campusSpinnerAdapterTo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campusLabels);
        campusSpinnerTo.setAdapter(campusSpinnerAdapterTo);

        campusSpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingSpinnerTo = (Spinner) findViewById(R.id.building_spinner_to);
                ArrayList<String> buildingLabels = new ArrayList<>();
                final Campus selectedCampus = position == 0 ? sgw : loyola;
                for (Building b : selectedCampus.getBuildings()) {
                    buildingLabels.add(b.getName());
                }
                ArrayAdapter<String> buildingSpinnerAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, buildingLabels.toArray(new String[0]));
                buildingSpinnerTo.setAdapter(buildingSpinnerAdapter);

                buildingSpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        floorSpinnerTo = (Spinner) findViewById(R.id.floor_spinner_to);
                        final Building selectedBuilding = selectedCampus.getBuildings().get(position);
                        targetBuilding = selectedBuilding;

                        ArrayList<String> floorLabels = new ArrayList<>();

                        if(selectedBuilding.getFloors().length != 0) {
                            for (Floor f : selectedBuilding.getFloors()) {
                                floorLabels.add(f.getFloorName());
                            }
                            ArrayAdapter<String> floorSpinnerAdapter = new ArrayAdapter<>(self, android.R.layout.simple_spinner_item, floorLabels.toArray(new String[0]));
                            floorSpinnerTo.setAdapter(floorSpinnerAdapter);
                        } else {
                            floorSpinnerTo.setAdapter(null);

                            roomSpinnerTo = (Spinner) findViewById(R.id.room_spinner_to);
                            roomSpinnerTo.setAdapter(null);

                            targetFloor = null;
                            targetRoom = null;
                        }

                        floorSpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                roomSpinnerTo = (Spinner) findViewById(R.id.room_spinner_to);

                                final Floor selectedFloor = selectedBuilding.getFloors()[position];
                                targetFloor = selectedFloor;

                                ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<>(self, android.R.layout.simple_dropdown_item_1line, selectedFloor.getRoomNames());
                                roomSpinnerTo.setAdapter(roomNameAdapter);

                                roomSpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                        targetRoom = selectedFloor.getRoomNames()[position];
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

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

        Button goButton = (Button) findViewById(R.id.from_to_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button fromToButton = (Button) findViewById(R.id.from_to_button);
                fromToButton.setVisibility(View.GONE);

                Spinner[] spinners = {(Spinner) findViewById(R.id.campus_spinner_from), (Spinner) findViewById(R.id.building_spinner_from), (Spinner) findViewById(R.id.floor_spinner_from), (Spinner) findViewById(R.id.room_spinner_from),
                        (Spinner) findViewById(R.id.campus_spinner_to), (Spinner) findViewById(R.id.building_spinner_to), (Spinner) findViewById(R.id.floor_spinner_to), (Spinner) findViewById(R.id.room_spinner_to)};

                for(Spinner s : spinners) {
                    s.setEnabled(false);
                }

                Button nextButton = (Button) findViewById(R.id.next_button);
                nextButton.setVisibility(View.VISIBLE);

                showDirections();
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //recursive function to show directions
    public void showDirections() {
        int floorMap = sourceFloor.getFloorMap();
        GraphBuilder gb = new GraphBuilder(getResources().getXml(floorMap), sourceFloor);
        List<Edge> edges = null;
        VectorDrawableCompat.VFullPath edge = null;
        Button nextButton = (Button) findViewById(R.id.next_button);

        if(sourceRoom.matches("^[a-zA-Z]\\d+(?:\\.\\d+)?")) {
            sourceRoom = sourceRoom.substring(1);
        }

        if(targetRoom.matches("^[a-zA-Z]\\d+(?:\\.\\d+)?")) {
            targetRoom = targetRoom.substring(1);
        }

        try {
            VectorChildFinder vector = new VectorChildFinder(this, floorMap, imageView);

            //is source and target are in the same building
            if(sourceBuilding.getName().equals(targetBuilding.getName())) {
                String localDestination;

                //if its on the same floor
                if(sourceFloor.getFloorLevel() == targetFloor.getFloorLevel()) {
                    //get edges to room
                    edges = gb.getShortestPath(sourceRoom, targetRoom);

                    localDestination = sourceBuilding.getInitials() + targetRoom;
                    localDestination = localDestination.toUpperCase();
                    colorMap(edges, vector, localDestination);

                    nextButton.setEnabled(false);
                }

                //if its on a lower floor
                else if(sourceFloor.getFloorLevel() > targetFloor.getFloorLevel()) {
                    //get edges to stairs/elevator doing down
                    edges = gb.getShortestPathFrom(sourceRoom, isHandicapped, GraphBuilder.Direction.DOWN);

                    if(isHandicapped) {
                        localDestination = sourceFloor.getGatewayNodes().getHandicappedDown();
                    } else {
                        localDestination = sourceFloor.getGatewayNodes().getNonHandicappedDown();
                    }

                    colorMap(edges, vector, localDestination);
                }

                //if its on a higher floor
                else {
                    //get edges to stairs/elevator doing up
                    edges = gb.getShortestPathFrom(sourceRoom, isHandicapped, GraphBuilder.Direction.UP);

                    if(isHandicapped) {
                        localDestination = sourceFloor.getGatewayNodes().getHandicappedUp();
                    } else {
                        localDestination = sourceFloor.getGatewayNodes().getNonHandicappedUp();
                    }

                    colorMap(edges, vector, localDestination);
                }
            }
        } catch (GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
        }
    }

    private void colorMap(List<Edge> edges, VectorChildFinder vector, String end) {
        VectorDrawableCompat.VFullPath edge;
        //draw edges onto map
        for(Edge e : edges) {
            edge = vector.findPathByName(e.getEdgeName());
            if (edge != null) {
                edge.setStrokeColor(Color.BLUE);
            }
        }

        //fill room color
        String room = sourceBuilding.getInitials() + sourceRoom;
        room = room.toUpperCase();
        edge = vector.findPathByName(room);
        if (edge != null) {
            edge.setFillColor(Color.BLUE);
        }

        edge = vector.findPathByName(end);
        if (edge != null) {
            edge.setFillColor(Color.YELLOW);
        }
    }

    public void setCampuses(Campus sgw, Campus loyola) {
        this.sgw = sgw;
        this.loyola = loyola;
        setUp(this);
    }

    public Spinner getCampusSpinnerFrom() {
        return campusSpinnerFrom;
    }

    public Spinner getBuildingSpinnerFrom() {
        return buildingSpinnerFrom;
    }

    public Spinner getFloorSpinnerFrom() {
        return floorSpinnerFrom;
    }

    public AutoCompleteTextView getRoomInput() {
        return roomInput;
    }
}
