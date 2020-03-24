package Models;

import android.graphics.drawable.Drawable;

import com.example.concordiaguide.R;

public class Floor {
    private String floorName;
    public int floorMap;
    public int floorLevel;
    private String[] roomNames;

    public Floor(String floorName, int floorLevel, int floorMap, String[] roomNames) {
        this.floorMap = floorMap;
        this.floorName = floorName;
        this.floorLevel = floorLevel;
        this.roomNames = roomNames;
    }

    public String getFloorName() {
        return floorName;
    }
    public String[] getRoomNames() {
        return roomNames;
    }
}
