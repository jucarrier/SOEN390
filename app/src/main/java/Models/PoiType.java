package Models;

public class PoiType {
    private int iconResource;
    private String type;

//will contain icon and type string to identify a POI category
    public PoiType(int iconResource, String type) {
        this.iconResource = iconResource;
        this.type = type;
    }

    public int getIconResource() {

        return iconResource;
    }

    public String getPoiType() {
        return type;
    }

}

