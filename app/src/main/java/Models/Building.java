package Models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Building {
    final String name;
    final String address;

    // First two hex digits are for opacity
    private final int fillColor = 0x4FAA0000;

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
