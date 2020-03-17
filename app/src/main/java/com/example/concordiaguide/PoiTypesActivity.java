package com.example.concordiaguide;

import android.os.Bundle;

import Helpers.PoiTypeAdapter;
import Models.PoiType;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PoiTypesActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_types);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PoiType[] types = new PoiType[] { //displays the three options so far
                new PoiType(R.drawable.ic_coffee, "Coffee Shop"),
                new PoiType(R.drawable.ic_bank, "Bank"),
                new PoiType(R.drawable.ic_restaurant, "Restaurant")
        };
        List<PoiType> mTypes = Arrays.asList(types);

        RecyclerView rc = (RecyclerView) findViewById(R.id.type_list);
        rc.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter poiAdapter = new PoiTypeAdapter(this, mTypes);
        rc.setAdapter(poiAdapter);
    }

}