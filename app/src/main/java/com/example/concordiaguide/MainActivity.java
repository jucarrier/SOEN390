package com.example.concordiaguide;

import Helpers.GoogleApiService;
import Helpers.PoiFragment;
import Models.Building;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Helpers.CampusBuilder;
import Helpers.ObjectWrapperForBinder;
import Models.Building;
import Models.Campus;
import Helpers.PlacesResult;
import Models.MyPlaces;
import Models.Results;

/**
 * This is the class that displays the map to the user. It is the one that is active when the app
 * is first opened.
 *
 * @param <locationManager> For use when locating the device
 */
public class MainActivity<locationManager> extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String drivingMethod = "driving";
    private static final int LOCATION_REQUEST = 500;
    protected static String preferredNavigationMethod = drivingMethod;
    private final String LAYOLA_NAME = "loyola";
    public Campus sgw;
    public Campus loyola;
    protected Cursor cursor;


    protected TabLayout transportationSelectionTab;
    //for finding current location
    //LatLng currentLocation; //to be filled in later by onLocationChanged
    double lat, lng;
    LatLng currentLocation = new LatLng(45.4967712, -73.5789604); //to be filled in later by onLocationChanged, this is a default location for testing with the emulator
    SupportMapFragment mapFragment;
    SearchView searchView;

    private TextView textViewAddressHere;  //this is the textView that will display the current building name
    private LocationManager locationManager;    //this is needed to find the user's current location
    private GoogleMap mMap;
    private DrawerLayout drawer;

    private boolean shuttle_active = false;
    private boolean showPOI = false;
    ArrayList<LatLng> listPoints;
    List<Results> results = new ArrayList<Results>();

    /**
     * this is the listener method that constantly updates the user's location for usage in other methods
     *
     * @param location The location of the user
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            currentLocation = new LatLng(lat, lng);
        }
        try {

            setContentView(R.layout.activity_maps);
            textViewAddressHere = findViewById(R.id.addressHere);

            if (textViewAddressHere == null) {
                System.out.println("latitude not found");
            } else {
                textViewAddressHere.setText("lat " + lat);
                System.out.println("lat " + lat);
            }
        } catch (Exception e) {
            System.out.println("begin \n" + e + "\n end");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //removing this will cause an error
    }

    @Override
    public void onProviderEnabled(String s) {
        //removing this will cause an error
    }

    @Override
    public void onProviderDisabled(String s) {
        //removing this will cause an error
    }

    /**
     * Method that is called when permissions are checked.
     *
     * @param requestCode  What type of request is requested
     * @param permissions  List of permissions that are handled
     * @param grantResults What permissions are accepted or denied
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
            default:
                // do nothing
                break;
        }
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //locate current location
        textViewAddressHere = findViewById(R.id.addressHere);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        boolean flag = false;
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.sv_location2);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(MainActivity.this);

                new FindAddressTask().execute(new FindAddressTaskParams(geocoder, addressList, mMap, location));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

        NavigationView navigation = findViewById(R.id.nav_viewer);

        // Here is where the menu elements are handled, change as you need
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            /**
             * Going to different activities depending on the option chosen
             * @param item
             * @return
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawer.closeDrawers();
                Intent intent = null;
                Bundle bundle;
                switch (id) {
                    case (R.id.menu_indoor_navigation):
                        bundle = new Bundle();
                        bundle.putBinder("sgw", new ObjectWrapperForBinder(sgw));
                        bundle.putBinder(LAYOLA_NAME, new ObjectWrapperForBinder(loyola));
                        intent = new Intent(getApplicationContext(), IndoorNavigationActivity.class).putExtras(bundle);
                        break;
                    case (R.id.menu_campus_navigation):
                        bundle = new Bundle();
                        bundle.putBinder("sgw", new ObjectWrapperForBinder(sgw));
                        bundle.putBinder(LAYOLA_NAME, new ObjectWrapperForBinder(loyola));
                        intent = new Intent(getApplicationContext(), CampusNavigationActivity.class).putExtras(bundle);
                        break;
                    case (R.id.menu_class_schedule):
                        intent = new Intent(getApplicationContext(), ClassScheduleActivity.class);
                        break;
                    case (R.id.find_POI):
                        intent = new Intent(getApplicationContext(), NearByPoiActivity.class);
                        break;
                    case (R.id.menu_to_sgw):
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sgw.center, 18));
                        break;
                    case (R.id.menu_to_loyola):
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loyola.center, 17));
                        break;
                    default:
                        // No option selected
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
                return false;
            }
        });

        listPoints = new ArrayList<>();

        Intent in = getIntent();
        Bundle b = in.getExtras();

        /*
        Building building;

        try {
            building = (Building) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("building")).getData();
            directionsToBuilding(building);
        } catch (Exception e) {
        }*/

        transportationSelectionTab = this.findViewById(R.id.transportationSelectionTab);

        String shuttle_direction;
        //this adds a listener to change the preferred navigation mode based on tab selection
        transportationSelectionTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedTab = tab.getContentDescription().toString();
                System.out.println(selectedTab);
                TaskRequestDirections tsk = new TaskRequestDirections();
                final CampusBuilder cb = new CampusBuilder(mMap);

                switch (selectedTab) {
                    case ("shuttle"):
                        startActivity(new Intent(getApplicationContext(), Shuttle.class));
                        break;
                    case (drivingMethod):
                        MainActivity.preferredNavigationMethod = drivingMethod;
                        break;
                    case ("publicTransportation"):
                        MainActivity.preferredNavigationMethod = "transit";
                        break;
                    default:
                        MainActivity.preferredNavigationMethod = "walking";
                        break;
                }
                if (listPoints.size() != 0) {
                    mMap.clear();
                    sgw = cb.buildSGW();
                    loyola = cb.buildLoyola();
                    tsk.execute(getRequestUrl(listPoints.get(0)));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //removing this will cause an error
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

        CardView cardViewNavigationPrompt = findViewById(R.id.cardViewNavigationPrompt);
        cardViewNavigationPrompt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = null;
                final Bundle bundle = new Bundle();
                bundle.putBinder("sgw", new ObjectWrapperForBinder(sgw));
                bundle.putBinder(LAYOLA_NAME, new ObjectWrapperForBinder(loyola));
                intent = new Intent(getApplicationContext(), CampusNavigationActivity.class).putExtras(bundle);
                startActivity(intent);
            }
        });
        /*
        long LOCATION_REFRESH_TIME = 20000;
        float LOCATION_REFRESH_DISTANCE = 5;
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);

        //zoom to current location as soon as the app opens
        */


        if (!flag) {
            long LOCATION_REFRESH_TIME = 20000;
            float LOCATION_REFRESH_DISTANCE = 5;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);
        }
    }


    /**---------------ADD THIS--------------
     * When the app is going to another activity needs to call back the main, it will go through this method instead of creating a new activity of the Main.
     *
     * @param intent captured from the activity from which the new intent is coming from
     *
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = intent.getExtras();
        shuttle_active = b.getBoolean("active");
        showPOI = b.getBoolean("Poi_fragment_bool");
        //Shuttle code
        if (shuttle_active==true) {
            mMap.clear();
            onMapReady(mMap);
        }
        else if(showPOI == true){
            mMap.clear();
            //listPoints.clear();
            results = PlacesResult.results;
            poiChosen(mMap);
            //onMapReady(mMap);
        }
        else
            onMapReady(mMap);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startAlarm(int hours, int minutes) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    /**
     * This method displays the path from a user's location to a building.
     *
     * @param building The building to be navigated to
     */
    public void directionsToBuilding(Building building) {
        AddressDecoder ad = new AddressDecoder();
        TaskRequestDirections trd = new TaskRequestDirections();
        LatLng dest;
        String reqUrl;

        dest = ad.getLocationFromAddress(building.getAddress());
        listPoints.add(dest);
        reqUrl = getRequestUrl(listPoints.get(0));
        trd.execute(reqUrl);
    }

    /**
     * This method gives the direction of the shuttle from one campus to another
     *
     * @param from The campus the user is leaving from
     * @param to The campus the user is going to
     */
    public void shuttleDirection(LatLng from, LatLng to) {
        TaskRequestDirections trd = new TaskRequestDirections();
        listPoints.add(to);
        String url = getRequestUrl_shuttle(from, to);

        MarkerOptions marker_from = new MarkerOptions();
        marker_from.position(from);
        MarkerOptions marker_to = new MarkerOptions();
        marker_to.position(to);
        marker_from.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));//Add second marker to the map
        marker_to.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(marker_from);
        mMap.addMarker(marker_to);

        trd.execute(url);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //@Override
    public void onLocateButtonPressed(View v) {
        AddressDecoder ad = new AddressDecoder();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.currentLocation, 18));
        //test here
        try {
            textViewAddressHere = findViewById(R.id.addressHere);
            String currentAddress = ad.getAddressFromLatLng(this.currentLocation.latitude, this.currentLocation.longitude);
            currentAddress = currentAddress.split(",")[0];  //processing to get a format that is easily matched with the list of buildings

            //uncomment this to test building detection if you are not currently near one of the campuses
            //this will set your current address to 1450 Guy and the card view should say 'John Molson'
            //currentAddress = "1450 Guy St";

            textViewAddressHere.setText(currentAddress);
            System.out.println(currentAddress); //show address in console for debugging


            for (Building b : sgw.getBuildings()) {
                if (b.getAddress().split(",")[0].equals(currentAddress)) {
                    textViewAddressHere.setText(b.getName());
                    break;
                } else {
                    textViewAddressHere.setText("Not on campus");
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private String getRequestUrl(LatLng dest) {
        //Value of origin
        String str_org = "origin=" + this.currentLocation.latitude + "," + this.currentLocation.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String insert = "mode=" + MainActivity.preferredNavigationMethod;
        System.out.println(insert);
        String mode = insert;
        String key = "key=AIzaSyBOlSFxzMbOCyNhbhOYBJ2XGoiMtS-OjbY ";
        //Build the full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String getRequestUrl_shuttle(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        String key = "key=AIzaSyBOlSFxzMbOCyNhbhOYBJ2XGoiMtS-OjbY ";
        //Build the full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param googleMap The map that is ready
     */


    /**---------------ADD THIS--------------
     *
     *
     *
     * @param googleMap
     */
    public void poiChosen(GoogleMap googleMap) {
        for (int i = 0; i < results.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            Results googlePlace = results.get(i);
            double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
            double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
            String placeName = googlePlace.getName();
            String vicinity = googlePlace.getVicinity();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // add marker to map
            googleMap.addMarker(markerOptions).showInfoWindow();;
            // move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(true);
            // googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setPadding(0, 0, 0, 350);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        final CampusBuilder cb = new CampusBuilder(mMap);
        sgw = cb.buildSGW();
        loyola = cb.buildLoyola();

        //map onclick listener - this will cause events to happen whenever you tap the main map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                searchView.clearFocus();
            }
        });

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {

            @Override
            public void onPolygonClick(Polygon polygon) {
                //used to send building object to popup activity
                final Bundle bundle = new Bundle();
                bundle.putBinder("building", new ObjectWrapperForBinder(polygon.getTag()));
                //go to popup activity
                startActivity(new Intent(MainActivity.this, BuildingInfoPopup.class).putExtras(bundle));
            }
        });

        if (shuttle_active == true) {
            LatLng from, to;
            try {
                from = (LatLng) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("From")).getData();
                to = (LatLng) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("To")).getData();
                shuttleDirection(from, to);
                shuttle_active = false;
            } catch (Exception e) {
            }
        }

        Building building;

        try {
            building = (Building) ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("building")).getData();
            directionsToBuilding(building);
        } catch (Exception e) {
        }

        //Add listener to polygons to show the building info popup
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (listPoints.size() > 0 || results.size()>0) {
                    listPoints.clear();
                    mMap.clear();
                    sgw = cb.buildSGW();
                    loyola = cb.buildLoyola();
                }
                //Save first point select
                listPoints.add(latLng);
                //Create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                mMap.addMarker(markerOptions);

                //Create the URL to get request to marker
                String url = getRequestUrl(listPoints.get(0));
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }
        });

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.currentLocation, 18), 1, null);   //zooms to current location in 1 ms, zoom level 18
        } catch (Exception e) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.495782, -73.579320), 18), 1, null);   //zooms to current location in 1 ms, zoom level 18
        }

    }

    private static class FindAddressTaskParams {
        Geocoder geocoder;
        List<Address> addressList;
        GoogleMap mMap;
        String location;

        FindAddressTaskParams(Geocoder geocoder, List<Address> addressList, GoogleMap mMap, String location) {
            this.geocoder = geocoder;
            this.addressList = addressList;
            this.mMap = mMap;
            this.location = location;
        }
    }

    private static class FindAddressTask extends AsyncTask<FindAddressTaskParams, Void, Address> {
        GoogleMap mMap;
        String location;

        protected Address doInBackground(FindAddressTaskParams... findAddressTaskParams) {
            FindAddressTaskParams params = findAddressTaskParams[0];
            if (!params.location.equals("")) {
                try {
                    params.addressList = params.geocoder.getFromLocationName(params.location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.mMap = params.mMap;
                this.location = params.location;
                if (params.addressList.size() != 0) {
                    return params.addressList.get(0);
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Address address) {
            if (address == null) {
                return;
            }
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            this.mMap.addMarker(new MarkerOptions().position(latlng).title(this.location));
            this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19));
        }
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
