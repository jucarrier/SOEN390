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
    final String initials;
    final Floor floors[];

    // First two hex digits are for opacity
    private final int fillColor = 0x4FAA0000;

    //Defines perimeter
    final Polygon polygon;

    public Building(GoogleMap mMap, String name, String address, String description, String initials, Floor[] floors, LatLng... lls) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.initials = initials;
        this.floors = floors;


        this.polygon = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(lls)
                .fillColor(fillColor));
        this.polygon.setTag(this);
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
    public String getInitials() { return initials; }

    public Floor getFloor(int level) {
        for (Floor f : floors) {
            if (f.floorLevel == level) {
                return f;
            }
        }
        return null;
    }
}
