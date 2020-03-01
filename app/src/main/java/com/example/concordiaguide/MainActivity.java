package com.example.concordiaguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Models.Building;
import Models.Campus;

<<<<<<< Updated upstream
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
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
                return params.addressList.get(0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Address address) {
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            this.mMap.addMarker(new MarkerOptions().position(latlng).title(this.location));
            this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19));
        }
    }

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;

    private Building hall;
    public Campus sgw;
    public Campus layola;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
=======
public class MainActivity<locationManager> extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    //for finding current location
    private TextView textView;  //this is the textView that will display the current building name
    private LocationManager locationManager;
    Location location = null;
    LatLng currentLocation; //to be filled in later by onLocationChanged
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //locate current location
        textView = (TextView) findViewById(R.id.addressHere);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
        }
>>>>>>> Stashed changes
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.sv_location);
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
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawer.closeDrawers();
                Intent intent = null;
                switch (id) {
                    case (R.id.menu_indoor_navigation):
                        intent = new Intent(getApplicationContext(), IndoorNavigationActivity.class);
                        break;
                    case (R.id.menu_campus_navigation):
                        intent = new Intent(getApplicationContext(), CampusNavigationActivity.class);
                        break;
                    case (R.id.menu_class_schedule):
                        intent = new Intent(getApplicationContext(), ClassScheduleActivity.class);
                        break;
                    case (R.id.menu_to_sgw):
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sgw.center, 18));
                        break;
                    case (R.id.menu_to_layola):
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(layola.center, 17));
                        break;

                }
                if (intent != null) {
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    //this is the listener method that constantly updates the user's location for usage in other methods
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        currentLocation = new LatLng(lat, lng);
        try {

            setContentView(R.layout.activity_maps);
            textView = (TextView) findViewById(R.id.addressHere);
            if((Object)textView == null){
                System.out.println("latitude not found");
            }
            textView.setText("lat " + lat);
            System.out.println("lat " + lat);
        } catch (Exception e){
            System.out.println("begin \n" +e+ "\n end");
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

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;

    public Campus sgw;
    public Campus loyola;
    private DrawerLayout drawer;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

<<<<<<< Updated upstream
=======
    //@Override
    public void onLocateButtonPressed(View view) {
        AddressDecoder ad = new AddressDecoder();
        currentLocation = new LatLng(lat, lng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.currentLocation, 18));
        //test here
        try{
            textView = (TextView) findViewById(R.id.addressHere);
            String currentAddress = ad.getAddressFromLatLng(this.currentLocation.latitude, this.currentLocation.longitude);
            currentAddress = currentAddress.split(",")[0];  //processing to get a format that is easily matched with the list of buildings

            //uncomment this to test building detection if you are not currently near one of the campuses
            //this will set your current address to 1450 Guy and the card view should say 'John Molson'
            //currentAddress = "1450 Guy St";

            textView.setText(currentAddress);
            System.out.println(currentAddress); //show address in console for debugging


            for(Building b: sgw.getBuildings()){
                if(b.getAddress().split(",")[0].equals(currentAddress)){
                    textView.setText(b.getName());
                    break;
                } else {
                    textView.setText("Not on campus");
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
>>>>>>> Stashed changes

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        this.hall = new Building(mMap, "Hall", "1455 Boulevard de Maisonneuve O, Montréal, QC H3G 1M8",
                new LatLng(45.497711, -73.579035),
                new LatLng(45.497373, -73.578311),
                new LatLng(45.496829, -73.578850),
                new LatLng(45.497165, -73.579551));

        this.sgw = new Campus(
                new ArrayList<Building>(Arrays.asList(hall)),
                new LatLng(45.496680, -73.578761));

        this.layola = new Campus(
                new ArrayList<Building>(),
                new LatLng(45.458239, -73.640462));
    }
}
