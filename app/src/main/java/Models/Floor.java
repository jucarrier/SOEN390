package Models;

import android.graphics.drawable.Drawable;

import com.example.concordiaguide.R;

public class Floor {
    public String floorName;
    public int floorMap;
    public int floorLevel;

    public Floor(String floorName, int floorLevel, int floorMap) {
        this.floorMap = floorMap;
        this.floorName = floorName;
        this.floorLevel = floorLevel;
    }
}
