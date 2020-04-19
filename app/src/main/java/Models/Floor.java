package Models;

import android.graphics.drawable.Drawable;

public class Floor {
    private String floorName;
    private int floorMap;
    public int floorLevel;
    private String[] roomNames;
    private GatewayNodes gatewayNodes;

    public Floor(String floorName, int floorLevel, int floorMap, GatewayNodes gatewayNodes, String[] roomNames) {
        this.floorMap = floorMap;
        this.floorName = floorName;
        this.floorLevel = floorLevel;
        this.roomNames = roomNames;
        this.gatewayNodes = gatewayNodes;
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
    public GatewayNodes getGatewayNodes() { return gatewayNodes; }
    public int getFloorLevel() { return floorLevel; }
}
