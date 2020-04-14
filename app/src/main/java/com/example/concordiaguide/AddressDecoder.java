package com.example.concordiaguide;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressDecoder extends AppCompatActivity {

    //this method uses the geocoder to get the user's current address using the gps coordinates
    //requires internet access and gps location permission enabled
    protected String getAddressFromLatLng(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        String out = "address not found";
        try {
            Geocoder geocoder = new Geocoder(this, Locale.CANADA);
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

        return out;
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        Address location;
        LatLng position = null;

        try {
            addresses = coder.getFromLocationName(strAddress, 5);
            if (addresses == null) {
                return null;
            }
            location = addresses.get(0);
            location.getLatitude();
            location.getLongitude();
            position = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {

        }

        return position;
    }
}
