package com.example.concordiaguide.Models;

import java.io.Serializable;

public class Geometry implements Serializable {

    private ViewLL v1;

    private Location location;

    public ViewLL getViewport() {
        return v1;
    }

    public void setViewport(ViewLL view1) {
        this.v1 = view1;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Results [viewport = " + v1 + ", location = " + location + "]";
    }
}
