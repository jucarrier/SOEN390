package com.example.concordiaguide;

import Models.Building;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import Helpers.ObjectWrapperForBinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Helpers.CampusBuilder;
import Models.Campus;

public class MainActivity<locationManager> extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    //for finding address
    private Button button;
    private TextView textView;
    private LocationManager locationManager;
    LatLng currentLocation; //to be filled in later by onLocationChanged

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        String out = "address not found";
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                out = addresses.get(0).getAddressLine(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        //return result.toString();
        return out;
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentLocation = new LatLng(lat, lng);

        System.out.println("lat " + lat + "\nlong " + lng);
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
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);



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
                        final Bundle bundle = new Bundle();
                        bundle.putBinder("sgw", new ObjectWrapperForBinder(sgw));
                        bundle.putBinder("loyola", new ObjectWrapperForBinder(loyola));
                        intent = new Intent(getApplicationContext(), CampusNavigationActivity.class).putExtras(bundle);
                        break;
                    case (R.id.menu_class_schedule):
                        intent = new Intent(getApplicationContext(), ClassScheduleActivity.class);
                        break;
                    case (R.id.menu_to_sgw):
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sgw.center, 18));
                        break;
                    case (R.id.menu_to_layola):
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loyola.center, 17));
                        break;

                }
                if (intent != null) {
                    startActivity(intent);
                }
                return false;
            }
        });



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
    public void onLocateButtonPressed(View view) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.currentLocation, 18));
        //test here
        try{
            textView = (TextView) findViewById(R.id.addressHere);
            String currentAddress = getAddress(this.currentLocation.latitude, this.currentLocation.longitude);
            currentAddress = currentAddress.split(",")[0];  //processing to get a format that is easily matched with the list of buildings

            //uncomment this to test if you are not currently near one of the campuses
            currentAddress = "1450 Guy St";

            textView.setText(currentAddress);
            //System.out.println(getAddress(this.currentLocation.latitude, this.currentLocation.longitude));

            //System.out.println(this.currentLocation.latitude);
            //System.out.println(currentAddress.get(0));

            for(Building b: sgw.getBuildings()){
                System.out.println(b.getAddress().split(",")[0]);
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }






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

        CampusBuilder cb = new CampusBuilder(mMap);
        sgw = cb.buildSGW();
        loyola = cb.buildLoyola();

        //Add listener to polygons to show the building info popup
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
    }


}
