package Models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Represents a building that is part of a campus in Concordia
 */
public class Building {
    final String name;
    final String address;
    final String description;
    final String initials;
    final Floor[] floors;

    // First two hex digits are for opacity
    private final int fillColor = 0x4FAA0000;

    //Defines perimeter
    Polygon polygon;

    /**
     * Constructor
     *
     * @param mMap GoogleMap on which the building will be drawn. Can be null
     * @param name Name of the building
     * @param address address of the building. Should be findable by Google Maps
     * @param description information about the building
     * @param initials How the building is abbreviated at Concordia. Not necessarily its actual initials.
     *                 (Example: Faubourg initials are FG, not F)
     * @param floors List of floors in the building
     * @param lls Latitude/longitude combinations representing the corners of the building. Used
     *            to represent the perimeter of the building
     */
    public Building(GoogleMap mMap, String name, String address, String description, String initials, Floor[] floors, LatLng... lls) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.initials = initials;
        this.floors = floors;

        if (mMap != null) {
            this.polygon = mMap.addPolygon(new PolygonOptions()
                    .clickable(true)
                    .add(lls)
                    .fillColor(fillColor));
            this.polygon.setTag(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getInitials() {
        return initials;
    }

    public Floor[] getFloors() {
        return floors;
    }

    public Floor getFloor(int level) {
        for (Floor f : floors) {
            if (f.floorLevel == level) {
                return f;
            }
        }
        return null;
    }
}
