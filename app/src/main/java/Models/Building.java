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
    final String description;

    private final int fillColor = 0x4FAA0000;
    private final double fillOpacity = 0.3;

    //Defines perimeter
    final Polygon polygon;

    public Building(GoogleMap mMap, String name, String address, String description, LatLng... lls) {
        this.name = name;
        this.address = address;
        this.description = description;


        this.polygon = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(lls)
                .fillColor(fillColor));
        this.polygon.setTag(this);

//        this.polygon.;
//        this.polygon.fillO
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
}
