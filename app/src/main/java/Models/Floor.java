package Models;

import android.graphics.drawable.Drawable;

public class Floor {
    private String floorName;
    private int floorMap;
    public int floorLevel;
    private String[] roomNames;
    private String handicappedStart;
    private String nonHandicappedStart;

    public Floor(String floorName, int floorLevel, int floorMap, String handicappedStart, String nonHandicappedStart, String[] roomNames) {
        this.floorMap = floorMap;
        this.floorName = floorName;
        this.floorLevel = floorLevel;
        this.roomNames = roomNames;
        this.handicappedStart = handicappedStart;
        this.nonHandicappedStart = nonHandicappedStart;
    }

    public String getFloorName() {
        return floorName;
    }
    public String[] getRoomNames() {
        return roomNames;
    }
    public int getFloorMap() {
        return floorMap;
    }
    public String getHandicappedStart() {
        return handicappedStart;
    }
    public String getNonHandicappedStart() {
        return nonHandicappedStart;
    }
    public int getFloorLevel() { return floorLevel; }
}
