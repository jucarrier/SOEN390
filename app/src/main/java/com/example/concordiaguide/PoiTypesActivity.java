package com.example.concordiaguide;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.concordiaguide.Adapter.PoiTypeAdapter;
import com.example.concordiaguide.Models.PoiType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PoiTypesActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_types);

        //taken from the POIType model(String iconResource,String itemType)
        PoiType[] types = new PoiType[] {
                new PoiType(R.drawable.ic_coffee, "Coffee Shop"),
                new PoiType(R.drawable.ic_bank, "Bank"),
                new PoiType(R.drawable.ic_restaurant, "Restaurant")
                };
        List<PoiType> mTypes = Arrays.asList(types);
        //converts our types array into a generic LIST of type POITYPE, called mTypes


        RecyclerView rc = (RecyclerView) findViewById(R.id.type_list);
        //displaying the categories as List in the recyclerview
        rc.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter poiAdapter = new PoiTypeAdapter(this, mTypes);
        //creates an instance of POITypeAdapter
        rc.setAdapter(poiAdapter);
        //sets the newly created PoiAdapter to display with the recyclerview
    }
}
