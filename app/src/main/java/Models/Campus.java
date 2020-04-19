package Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Represents a campus at Concordia. There are two at Concordia: SGW and Layola. If this project
 * was extended to support other universities this class would see more use.
 */
public class Campus {
    //Where to zoom in
    public final LatLng center;
    List<Building> buildings;

    public Campus(List<Building> buildings, LatLng center) {
        this.buildings = buildings;
        this.center = center;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public Building getBuilding(String name) {
        for (Building b : buildings) {
            if (b.name.equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }
}
