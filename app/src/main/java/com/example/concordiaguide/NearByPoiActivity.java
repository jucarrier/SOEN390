package com.example.concordiaguide;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import Helpers.PoiFragment;

/**
 * This class allows the user to choose the nearby point of interest from a dropdown menu(spinner view)
 */
public class NearByPoiActivity extends AppCompatActivity {
    public PoiFragment poiFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_poi_activity);

        poiFragment= new PoiFragment();

        loadFragment(poiFragment);
    }

    /**
     * This method is for loading the nearby_poi_layout onto poiFragment for display
     * @param fragment
     */
    public void loadFragment(Fragment fragment){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction fmt= fm.beginTransaction();
        fmt.replace(R.id.nearby_poi_layout,fragment);
        fmt.commit();
    }

}
