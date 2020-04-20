package com.example.concordiaguide;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Helpers.GraphBuilder;
import Helpers.ObjectWrapperForBinder;
import Models.Building;
import Models.Campus;
import Models.Edge;
import Models.Floor;


/**
 * The IndoorNavigationActivity describes how the indoor navigation is handled.
 * It contains the methods to find the optimal path between rooms in a floor as well as
 * highlight that path.
 */
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

    private final String TAG = "IndoorNavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppCompatActivity self = this;
        setContentView(R.layout.activity_indoor_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        imageView = findViewById(R.id.floor_view);
        try {
            sgw = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("sgw")).getData();
            loyola = (Campus) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("loyola")).getData();
        } catch (Exception e) {
            return;
        }
        setUp(self);
    }

    /**
     * This method is called on creation. It gets the campus building and floor data it needs
     * to present to the user so they can select the room they want to navigate to.
     * Also sets up the spinners displayed in the activity.
     * @param self
     */
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
                                // Purposely do nothing
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Purposely do nothing
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Purposely do nothing
            }
        });

        CheckBox handicappedCheckbox = (CheckBox) findViewById(R.id.is_handicapped);
        handicappedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHandicapped = b;
            }
        });

        Button goButton = (Button) findViewById(R.id.from_to_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sourceFloor == null || sourceRoom == null || targetFloor == null || targetRoom == null) {
                    Log.d(TAG, "Target/Source Floor/Room was not selected");
                    return;
                }

                Button fromToButton = (Button) findViewById(R.id.from_to_button);
                fromToButton.setText("Next");
                fromToButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Source: " + sourceRoom);
                        Log.d(TAG, "Target: " + targetRoom);
                        showDirections();
                    }
                });

                Spinner[] spinners = {(Spinner) findViewById(R.id.campus_spinner_from), (Spinner) findViewById(R.id.building_spinner_from), (Spinner) findViewById(R.id.floor_spinner_from), (Spinner) findViewById(R.id.room_spinner_from),
                        (Spinner) findViewById(R.id.campus_spinner_to), (Spinner) findViewById(R.id.building_spinner_to), (Spinner) findViewById(R.id.floor_spinner_to), (Spinner) findViewById(R.id.room_spinner_to)};

                for(Spinner s : spinners) {
                    s.setEnabled(false);
                }

                CheckBox cb = (CheckBox) findViewById(R.id.is_handicapped);
                cb.setEnabled(false);

                showDirections();
            }
        });

        Button newButton = (Button) findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This function is the inter floor logic for the directions from the selected source room to the selected target room in steps (each floor is a step).
     * Steps can be navigated with the help of the Next button.
     */
    public void showDirections() {
        int floorMap = sourceFloor.getFloorMap();
        imageView.setImageResource(floorMap);
        GraphBuilder gb = new GraphBuilder(getResources().getXml(floorMap), sourceFloor);
        List<Edge> edges = null;
        Button goButton = (Button) findViewById(R.id.from_to_button);

        if(sourceRoom.matches("^[a-zA-Z]\\d.*")) {
            sourceRoom = sourceRoom.substring(1);
        } else if(sourceRoom.matches("^[a-zA-Z]{2}\\d.*")) {
            sourceRoom = sourceRoom.substring(2);
        } else if(sourceRoom.matches("^[a-zA-Z]{3}\\d.*")) {
            sourceRoom = sourceRoom.substring(3);
        }

        if(targetRoom.matches("^[a-zA-Z]\\d.*")) {
            targetRoom = targetRoom.substring(1);
        } else if(targetRoom.matches("^[a-zA-Z]{2}\\d.*")) {
            targetRoom = targetRoom.substring(2);
        } else if(targetRoom.matches("^[a-zA-Z]{3}\\d.*")) {
            targetRoom = targetRoom.substring(3);
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

                    if(targetRoom.matches("^\\d+.*")) {
                        localDestination = sourceBuilding.getInitials() + targetFloor.getInitials() + targetRoom;
                        localDestination = localDestination.toUpperCase();
                    } else { localDestination = targetRoom; }
                    colorMap(edges, vector, localDestination);

                    goButton.setEnabled(false);
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

                    try {
                        sourceFloor = sourceBuilding.getFloor(sourceFloor.getFloorLevel() - 1);
                        if(isHandicapped) {
                            sourceRoom = sourceFloor.getGatewayNodes().getHandicappedDown();
                        } else {
                            sourceRoom = sourceFloor.getGatewayNodes().getNonHandicappedDown();
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "No file for Floor below found");
                    }
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

                    try {
                        sourceFloor = sourceBuilding.getFloor(sourceFloor.getFloorLevel() + 1);
                        if(isHandicapped) {
                            sourceRoom = sourceFloor.getGatewayNodes().getHandicappedUp();
                        } else {
                            sourceRoom = sourceFloor.getGatewayNodes().getNonHandicappedUp();
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "No file for Floor above found");
                    }
                }
            }

            //if not in the same building
            else {
                edges = gb.getShortestPath(sourceRoom, sourceFloor.getGatewayNodes().getOutside());
                colorMap(edges, vector, sourceFloor.getGatewayNodes().getOutside());
                if(sourceFloor.isMainFloor()) {
                    goButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Bundle bundle = new Bundle();
                            bundle.putBinder("userToBuilding", new ObjectWrapperForBinder(targetBuilding));
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtras(bundle));
                        }
                    });
                }
            }
        } catch (GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function changes the color of the vector elements to display the path from source room to target room.
     * @param edges
     * @param vector
     * @param end
     */
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
        String room;
        if(sourceRoom.matches("^\\d+.*")) {
            room = sourceBuilding.getInitials() + sourceFloor.getInitials() + sourceRoom;
            room = room.toUpperCase();
        } else { room = sourceRoom; }
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

    /**
     * This function is used for test purposes.
     * @return
     */
    public Spinner getCampusSpinnerFrom() {
        return campusSpinnerFrom;
    }

    /**
     * This function is used for test purposes.
     * @return
     */
    public Spinner getBuildingSpinnerFrom() {
        return buildingSpinnerFrom;
    }

    /**
     * This function is used for test purposes.
     * @return
     */
    public Spinner getFloorSpinnerFrom() {
        return floorSpinnerFrom;
    }

    /**
     * This function is used for test purposes.
     * @param building
     * @param floor
     * @param room
     */
    public void setSource(Building building, Floor floor, String room) {
        this.sourceBuilding = building;
        this.sourceFloor = floor;
        this.sourceRoom = room;
    }

    /**
     * This function is used for test purposes.
     * @param building
     * @param floor
     * @param room
     */
    public void setTarget(Building building, Floor floor, String room) {
        this.targetBuilding = building;
        this.targetFloor = floor;
        this.targetRoom = room;
    }

    /**
     * This function is used for test purposes.
     * @param handicapped
     */
    public void setHandicapped(boolean handicapped) {
        this.isHandicapped = handicapped;
    }

    /**
     * This function is used for test purposes.
     * @return
     */
    public ImageView getImageView() { return imageView; }
}
