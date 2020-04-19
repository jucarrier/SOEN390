package com.example.concordiaguide;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import Helpers.PlacesResult;
import Models.Results;

import java.util.ArrayList;
import java.util.List;

public class ShowPlacesOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    List<Results> results = new ArrayList<Results>();
    //when clicked show the list of places on map, this activity puts a marker on all the suggested POI's
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        results = PlacesResult.results;
        Toast.makeText(this, String.valueOf(results.size()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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
            googleMap.addMarker(markerOptions).showInfoWindow();
            // move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(true);
           // googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }
}

