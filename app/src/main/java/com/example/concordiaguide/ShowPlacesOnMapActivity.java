package com.example.concordiaguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Helpers.PlacesResult;
import Models.Results;

public class ShowPlacesOnMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener
{

    List<Results> results = new ArrayList<>();


    //when clicked show the list of places on map, this activity puts a marker on all the suggested POI's
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        results = PlacesResult.results;
        Toast.makeText(this, String.valueOf(results.size()), Toast.LENGTH_LONG).show();

        if(results.size() == 0)
        {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Selected point of interest not found within the chosen distance");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            mapFragment.getView().setVisibility(View.GONE);

        }

    }

    /**
     * All the nearest point of interests markers are displayed on the map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        googleMap.setOnInfoWindowClickListener(this);
        for (int i = 0; i < results.size(); i++)
        {


            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.snippet(results.get(i).getName());
            Results googlePlace = results.get(i);
            double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
            double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
            String placeName = googlePlace.getName();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // add marker to map
            googleMap.addMarker(markerOptions).showInfoWindow();
            // move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(true);
        }


    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        String placeNAme = marker.getSnippet();
        showAlertNaviagtion(marker.getPosition().latitude, marker.getPosition().longitude,placeNAme);

    }

    /**
     * Allows the user to choose navigation mode other than walking. It leads the user to actual google maps
     * @param lat
     * @param lng
     * @param placeNAme
     */
    private void showAlertNaviagtion(Double lat,Double lng,String placeNAme) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowPlacesOnMapActivity.this);
        alertDialog.setMessage("Navigate this address?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which)
            {
                Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + Uri.encode(placeNAme)); //
                Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                if (intent.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent);
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }



}

