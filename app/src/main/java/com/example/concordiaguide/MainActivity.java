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
        setupBuildings();
    }

    public void setupBuildings() {
        Building hall = new Building(mMap, "Hall", "1455 Boulevard de Maisonneuve O, Montréal, QC H3G 1M8",
                new LatLng(45.497711, -73.579035),
                new LatLng(45.497373, -73.578311),
                new LatLng(45.496829, -73.578850),
                new LatLng(45.497165, -73.579551));

        Building jm = new Building(mMap, "John Molson", "1450 Guy St, Montreal, Quebec H3H 0A1",
                new LatLng(45.495166, -73.579171),
                new LatLng(45.495222, -73.579113),
                new LatLng(45.495361, -73.579370),
                new LatLng(45.495531, -73.579196),
                new LatLng(45.495446, -73.578952),
                new LatLng(45.495188, -73.578517),
                new LatLng(45.495002, -73.578733),
                new LatLng(45.495032, -73.578789),
                new LatLng(45.495002, -73.578818),
                new LatLng(45.495166, -73.579171));

        Building gm = new Building(mMap, "GM", "1550 De Maisonneuve West, 1550 Boulevard de Maisonneuve O, Montreal, Quebec H3G 1N1",
                new LatLng(45.495780, -73.579145),
                new LatLng(45.496132, -73.578807),
                new LatLng(45.495947, -73.578429),
                new LatLng(45.495617, -73.578741),
                new LatLng(45.495780, -73.579087),
                new LatLng(45.495764, -73.579110),
                new LatLng(45.495780, -73.579145));

        Building ev = new Building(mMap, "EV", "1493-1515 Saint-Catherine St W, Montreal, Quebec H3G 2W1",
                new LatLng(45.495863, -73.578497),
                new LatLng(45.495440, -73.577609),
                new LatLng(45.495188, -73.577876),
                new LatLng(45.495247, -73.578019),
                new LatLng(45.495592, -73.578765),
                new LatLng(45.495863, -73.578497));

        Building lb = new Building(mMap, "Library Building", "Pavillion J.W. McConnell Bldg, 1400 Maisonneuve Blvd W, Montreal, Quebec H3G 1M8",
                new LatLng(45.496729, -73.578579),
                new LatLng(45.497259, -73.578058),
                new LatLng(45.496893, -73.577294),
                new LatLng(45.496616, -73.577557),
                new LatLng(45.496637, -73.577602),
                new LatLng(45.496584, -73.577651),
                new LatLng(45.496496, -73.577466),
                new LatLng(45.496256, -73.577699),
                new LatLng(45.496668, -73.578567),
                new LatLng(45.496706, -73.578531),
                new LatLng(45.496729, -73.578579));

        Building td = new Building(mMap, "TD Building", "1410 Guy St, Montreal, Quebec H3H 2L7",
                new LatLng(45.495128, -73.578501),
                new LatLng(45.495189, -73.578428),
                new LatLng(45.495037, -73.578074),
                new LatLng(45.494944, -73.578176),
                new LatLng(45.495023, -73.578325),
                new LatLng(45.495043, -73.578301),
                new LatLng(45.495065, -73.578342),
                new LatLng(45.495048, -73.578365),
                new LatLng(45.495128, -73.578501));

        Building fg = new Building(mMap, "FG Building", "1616 Saint-Catherine St W, Montreal, Quebec H3H 1L7",
                new LatLng(45.494911, -73.577786),
                new LatLng(45.494655, -73.577222),
                new LatLng(45.494399, -73.577521),
                new LatLng(45.494453, -73.577620),
                new LatLng(45.493626, -73.578728),
                new LatLng(45.493823, -73.579067),
                new LatLng(45.494911, -73.577786));

        Building gn = new Building(mMap, "Grey Nun's Building", "1190 Guy St, Montreal, Quebec H3H 2L4",
                new LatLng(45.493974, -73.577561),
                new LatLng(45.494125, -73.577414),
                new LatLng(45.494095, -73.577349),
                new LatLng(45.494392, -73.577059),
                new LatLng(45.494020, -73.576281),
                new LatLng(45.494124, -73.576180),
                new LatLng(45.494034, -73.575996),
                new LatLng(45.493934, -73.576095),
                new LatLng(45.493713, -73.575641),
                new LatLng(45.493571, -73.575783),
                new LatLng(45.493792, -73.576244),
                new LatLng(45.493492, -73.576540),
                new LatLng(45.493470, -73.576499),
                new LatLng(45.493341, -73.576625),
                new LatLng(45.493364, -73.576674),
                new LatLng(45.493025, -73.577007),
                new LatLng(45.492733, -73.576396),
                new LatLng(45.492591, -73.576539),
                new LatLng(45.492741, -73.576861),
                new LatLng(45.492670, -73.576938),
                new LatLng(45.492711, -73.577033),
                new LatLng(45.492810, -73.576962),
                new LatLng(45.492896, -73.577142),
                new LatLng(45.492840, -73.577196),
                new LatLng(45.492927, -73.577379),
                new LatLng(45.492993, -73.577315),
                new LatLng(45.493088, -73.577510),
                new LatLng(45.493198, -73.577401),
                new LatLng(45.493108, -73.577212),
                new LatLng(45.493425, -73.576907),
                new LatLng(45.493530, -73.577129),
                new LatLng(45.493635, -73.577262),
                new LatLng(45.493747, -73.577157),
                new LatLng(45.493869, -73.577340));

        Building va = new Building(mMap, "VA Building", "1395 René-Lévesque Blvd W, Montreal, Quebec H3G 2M5",
                new LatLng(45.495672, -73.574309),
                new LatLng(45.496185, -73.573799),
                new LatLng(45.496070, -73.573563),
                new LatLng(45.495816, -73.573811),
                new LatLng(45.495667, -73.573506),
                new LatLng(45.495404, -73.573765),
                new LatLng(45.495672, -73.574309));

        sgw = new Campus(
                new ArrayList<>(Arrays.asList(hall, jm, gm, ev, lb, td, fg, gn, va)),
                new LatLng(45.496680, -73.578761));

        layola = new Campus(
                new ArrayList<Building>(),
                new LatLng(45.458239, -73.640462));
    }
}
