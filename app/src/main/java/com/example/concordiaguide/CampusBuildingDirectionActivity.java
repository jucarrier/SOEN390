package com.example.concordiaguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Helpers.CampusBuilder;
import Helpers.PoiFragment;
import Models.Building;
import Models.Campus;
import Models.Floor;

public class CampusBuildingDirectionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker marker;
    private double lat, lng;
    private String name;
    private String address;
    private String description;
    private String initials;
    ArrayList<LatLng> listPoints;
    private LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);//use activity_maps

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            name = bundle.getString("name");
            address = bundle.getString("address");
            description = bundle.getString("description");
            initials = bundle.getString("initials");
        }

        currentLocation = new LatLng(lat, lng); // user location

    }

    public Campus sgw;
    public Campus loyola;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setPadding(0, 0, 0, 350);

        googleMap.setMyLocationEnabled(true);

        final CampusBuilder cb = new CampusBuilder(googleMap);
        sgw = cb.buildSGW();
        loyola = cb.buildLoyola();

        try {
            directionsToBuilding(address);
        } catch (Exception e) {
        }

        //showRoute();
    }

    private void showRoute() {
        LatLng currentPosition = new LatLng(lat, lng); // user location

    }

    public void directionsToBuilding(String address) {
        AddressDecoder ad = new AddressDecoder();
        TaskRequestDirections trd = new TaskRequestDirections();
        LatLng dest;
        String reqUrl;

        dest = ad.getLocationFromAddress(address);
        listPoints.add(dest);
        reqUrl = getRequestUrl(listPoints.get(0));
        trd.execute(reqUrl);
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
                googleMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
