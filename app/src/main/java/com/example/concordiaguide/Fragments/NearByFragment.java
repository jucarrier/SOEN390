package com.example.concordiaguide.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import Helpers.ObjectWrapperForBinder;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.concordiaguide.Models.PoiType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.concordiaguide.Adapter.PlaceRecyclerViewAdapter;
import com.example.concordiaguide.Services.RtfBuilder;
import com.example.concordiaguide.Services.GoogleApiService;
import com.example.concordiaguide.Models.MyPlaces;
import com.example.concordiaguide.R;
import com.example.concordiaguide.PlacesResult;
import com.example.concordiaguide.ShowPlacesOnMapActivity;
import com.example.concordiaguide.Adapter.PoiTypeAdapter;

public class NearByFragment extends Fragment {

    private ImageView imageViewSearch;
    private Spinner spinner_nearby_choices;

    private RecyclerView recyclerViewPlaces;
    private LinearLayout linearLayoutShowOnMap;
    double latitude;
    double longitude;


    private FusedLocationProviderClient flc; //helps to find last know location
    LocationManager lm;
    LocationManager locationManager;

    double lat = 0;
    double lng = 0;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;
    //currently using these as test, can add more types later on.
    public static final String TYPE_CAFE="cafe";
    public static final String TYPE_BANK="bank";
    public static final String TYPE_RESTAURANT="restaurant";

    private static final String TAG="locationservice";

   /* PoiType poi= (PoiType)((ObjectWrapperForBinder)getIntent().getExtras().getBinder(key:"type")).getData();*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        //all these references can be found on fragment_near_by.xml
        spinner_nearby_choices = view.findViewById(R.id.spinner_nearby_choices);
        imageViewSearch = view.findViewById(R.id.imageViewSearch);
        recyclerViewPlaces = view.findViewById(R.id.recyclerViewPlaces);
        linearLayoutShowOnMap = view.findViewById(R.id.linearLayoutShowOnMap);

        locationService();
        //onclick to the search button, following occurs
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = spinner_nearby_choices.getSelectedItemPosition();
                if (position == 0) {
                    Toast.makeText(getContext(), "Please select valid type", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    placeType = spinner_nearby_choices.getSelectedItem().toString();
                    switch (placeType) {
                        case "Coffee Shop": {
                            placeType = TYPE_CAFE;
                            getNearByPlaces();
                            break;
                        }
                        case "Bank": {
                            placeType = TYPE_BANK;
                            getNearByPlaces();
                            break;
                        }
                        case "Restaurant": {
                            placeType = TYPE_RESTAURANT;
                            getNearByPlaces();
                            break;
                        }

                    }
                }
            }
        });

        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacesResult.results = myPlaces.getResults();
                Intent intent = new Intent(getContext(), ShowPlacesOnMapActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
    private void locationService(){

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            Log.d(TAG, "locationService: Fetching data from gps");

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new NearByFragment.MyLocationListener();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }//if the permissions don't match, do nothing

            flc = LocationServices.getFusedLocationProviderClient(getContext());

            flc.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();

                    } else {//show users location
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                        } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "GPS off", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override //breaks down location coordinates and maps them to lattitude/longitude variables
        public void onLocationChanged(Location lc) {
            longitude = lc.getLongitude();
            latitude = lc.getLatitude();

            lat = lc.getLatitude();
            lng = lc.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    //this method helps to build the url that would be passed for HTTP requests
    private String buildUrl(double latitude, double longitude, String API_KEY){

        StringBuilder urlStr = new StringBuilder("api/place/search/json?");

        urlStr.append("&location=");
        urlStr.append(Double.toString(latitude));
        urlStr.append(",");
        urlStr.append(Double.toString(longitude));
        urlStr.append("&radius=500"); // places between 5 kilometer
        urlStr.append("&types=" + placeType.toLowerCase());
        urlStr.append("&sensor=false&key=" + API_KEY);

        return urlStr.toString();
    }

    private void getNearByPlaces(){
        String apiKey = getContext().getResources().getString(R.string.api_key);
        String url = buildUrl(lat, lng, apiKey);
        Log.d("finalUrl", url);


        googleApiService = RtfBuilder.builder().create(GoogleApiService.class);

        Call<MyPlaces> call = googleApiService.getNearByPlaces(url);


        call.enqueue(new Callback<MyPlaces>() {
            @Override
            public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                Log.d("MyPlaces", response.body().toString());
                myPlaces = response.body();
                Log.d("MyPlaces", myPlaces.getResults().get(0).toString());

                PlaceRecyclerViewAdapter adapter = new PlaceRecyclerViewAdapter(getContext(), myPlaces, lat, lng);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerViewPlaces.setLayoutManager(layoutManager);
                recyclerViewPlaces.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPlaces.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                linearLayoutShowOnMap.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MyPlaces> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
