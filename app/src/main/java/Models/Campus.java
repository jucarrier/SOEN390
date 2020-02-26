package Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Campus {
    final List<Building> buildings;

    //Where to zoom in
    final LatLng center;

    public Campus(List<Building> buildings, LatLng center) {
        this.buildings = buildings;
        this.center = center;
    }
}
