package Models;

/**
 * Represents one floor in a building that is part of Concordia.
 */
public class Floor {
    public int floorLevel;
    private String floorName;
    private int floorMap;
    private String[] roomNames;
    private GatewayNodes gatewayNodes;

    /**
     * @param floorName Name of the floor as it would be described over text or in speech.
     *                  (Ground, 8th, tunnel)
     * @param floorLevel Number representing the height of the floor in regards to floor level
     *                   with 1 representing the floor level. One level underground is -1. One level
     *                   above ground floor is 2.
     * @param floorMap The drawable that represents the floor
     * @param gatewayNodes Nodes that represent the ways to get in or out of the floor (elevators,
     *                     stairs, escalators, doors to outside)
     * @param roomNames Names of the rooms that are in the floor
     */
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

    public GatewayNodes getGatewayNodes() {
        return gatewayNodes;
    }

    public int getFloorLevel() {
        return floorLevel;
    }
}
