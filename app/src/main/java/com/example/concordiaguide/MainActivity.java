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
import java.util.List;

import Helpers.CampusBuilder;
import Models.Campus;

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
    }
}
