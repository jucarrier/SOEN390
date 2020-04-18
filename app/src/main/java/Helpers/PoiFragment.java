package Helpers;

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
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.concordiaguide.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import Models.MyPlaces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import Helpers.PlaceRecyclerViewAdapter;
import Helpers.RtfBuilder;
import Helpers.GoogleApiService;

import com.example.concordiaguide.R;
import Helpers.PlacesResult;
import com.example.concordiaguide.ShowPlacesOnMapActivity;

/**
 * this fragment is loaded upon call from the NearbyPoiActivity.
 * it sets up the spinner view to display the type of places we want to see
 *
 */
public class PoiFragment extends Fragment {
    private ImageView imageViewSearch;
    private RecyclerView recyclerViewPlaces;
    private LinearLayout linearLayoutShowOnMap;

    public double latitude,longitude;
    double lat = 0;
    double lng = 0;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;

    private FusedLocationProviderClient flc;
    LocationManager lm;
    LocationManager locationManager;

    private Spinner spinner_nearby_choices;

    public static final String TYPE_METRO="subway_station";
    public static final String TYPE_BANK="bank";
    public static final String TYPE_PHARMACY="pharmacy";
    public static final String TYPE_GYM="gym";
    public static final String TYPE_CAFE="cafe";
    public static final String TYPE_BAR="bar";
    public static final String TYPE_RESTAURANT="restaurant";


    private static final String TAG="locationservice";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        //all these references can be found on fragment_near_by.xml
        spinner_nearby_choices = view.findViewById(R.id.spinner_nearby_choices);
        imageViewSearch = view.findViewById(R.id.imageViewSearch);
        recyclerViewPlaces = view.findViewById(R.id.recyclerViewPlaces);
        linearLayoutShowOnMap = view.findViewById(R.id.linearLayoutShowOnMap);

        locationService();//checks for location of the user

        //when the search button is pressed on the spinner
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = spinner_nearby_choices.getSelectedItemPosition();
                //if no POI was chosen, show the following toast message
                if (position == 0) {
                    Toast.makeText(getContext(), "Please select valid type", Toast.LENGTH_SHORT).show();
                } else {
                    //when a Poi type is chosen and the search button is pressed, execute getNearbyPlaces function
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
                        case "Pharmacy": {
                            placeType = TYPE_PHARMACY;
                            getNearByPlaces();
                            break;
                        }
                        case "Bar": {
                            placeType = TYPE_BAR;
                            getNearByPlaces();
                            break;
                        }
                        case "Metro": {
                            placeType = TYPE_METRO;
                            getNearByPlaces();
                            break;
                        }
                        case "Gym": {
                            placeType = TYPE_GYM;
                            getNearByPlaces();
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        });

        //on press would show  showPlacesOnMap activity -> All places appeared on seach will be shown with a marker on the map
        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacesResult.results = myPlaces.getResults();
                Intent intent = new Intent(getContext(), ShowPlacesOnMapActivity.class);
                startActivity(intent);
                /*final Bundle bundle= new Bundle();
                bundle.putBoolean("Poi_fragment_bool", true);
                Intent intent2= new Intent(getContext(), MainActivity.class).putExtras(bundle);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);*/
            }
        });

        return view;

    }


    /**
     * breaks down location coordinates and maps them to lattitude/longitude variables
     */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location lc) {
            longitude = lc.getLongitude();
            latitude = lc.getLatitude();

            lat = lc.getLatitude();
            lng = lc.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Required for interface implementation. Not necessary for our purposes.
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Required for interface implementation. Not necessary for our purposes.
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Required for interface implementation. Not necessary for our purposes.
        }
    }

    /**
     * checks for user's location acquired from the GPS data
     */
    private void locationService(){

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            Log.d(TAG, "locationService: Fetching data from gps");

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new PoiFragment.MyLocationListener();
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
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

                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    /**
     * @param latitude
     * @param longitude
     * @param API_KEY
     * @return
     * this method helps to build the url that would be passed for HTTP requests
     */

    private String buildUrl(double latitude, double longitude, String API_KEY){

        StringBuilder urlStr = new StringBuilder("api/place/search/json?");

        urlStr.append("&location=");
        urlStr.append(Double.toString(latitude));
        urlStr.append(",");
        urlStr.append(Double.toString(longitude));
        urlStr.append("&radius=500"); // places between 5 kilometer
        urlStr.append("&types=" + placeType.toLowerCase());//takes the type from the switch cases
        urlStr.append("&sensor=false&key=" + API_KEY);

        return urlStr.toString();
    }

    /**
     * This method plugs the location url into the RTFbuilder function in order to parse them
     * and show them in a list/recycler view
     */
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
              //  Log.d("MyPlaces", myPlaces.getResults().get(0).toString());

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

