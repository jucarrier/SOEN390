package Models;

public class PoiType {
    private int iconResource;
    private String type;

    public PoiType(int iconResource, String type) {
        this.iconResource = iconResource;
        this.type = type;
    }

    public int getIconResource() {
        return iconResource;
    }

    public String getType() {
        return type;
    }

}

