package com.example.concordiaguide.Models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Building {
    final String name;
    final String address;

    private final int fillColor = 0x4FAA0000;
    private final double fillOpacity = 0.3;

    //Defines perimeter
    final Polygon polygon;

    public Building(GoogleMap mMap, String name, String address, LatLng... lls) {
        this.name = name;
        this.address = address;


        this.polygon = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(lls)
                .fillColor(fillColor));
//        this.polygon.;
//        this.polygon.fillO
    }
}