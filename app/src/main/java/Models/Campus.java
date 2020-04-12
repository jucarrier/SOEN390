package Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

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
            if (b.name.toLowerCase().equals(name.toLowerCase())) {
                return b;
            }
        }
        return null;
    }
}
