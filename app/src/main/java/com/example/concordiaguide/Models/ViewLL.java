package com.example.concordiaguide.Models;

import java.io.Serializable;

public class ViewLL implements Serializable {


    private LatLongNE northeast;
    private LatLongSW southwest;

    public LatLongNE getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLongNE northeast) {
        this.northeast = northeast;
    }
    public LatLongSW getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLongSW southwest) {
        this.southwest = southwest;
    }

    @Override
    public String toString() {
        return "Results [southwest = " + southwest + ", northeast = " + northeast + "]";
    }
}
