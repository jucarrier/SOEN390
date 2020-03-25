package com.example.concordiaguide.Models;

public class PoiType {

    private int iconRes;
    private String type;

    public int getIconRes() {
        return iconRes;
    }

    public String getType() {
        return type;
    }



    public PoiType(int iconResource, String itemType){
        iconRes=iconResource;
        type= itemType;
    }

}
