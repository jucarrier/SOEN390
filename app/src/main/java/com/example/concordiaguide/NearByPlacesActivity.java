package com.example.concordiaguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.concordiaguide.Fragments.NearByFragment;
import com.example.concordiaguide.Models.PoiType;
import com.google.android.material.tabs.TabLayout;
import com.example.concordiaguide.Adapter.PoiTypeAdapter;

import com.example.concordiaguide.Adapter.*;

import java.util.Objects;

import Helpers.ObjectWrapperForBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


public class NearByPlacesActivity extends AppCompatActivity {

   // public TabLayout tabLayout;
   // public ViewPager viewPager;
    public NearByFragment nearByFragment;

    Bundle bundle= getIntent().getExtras();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set to transition to desired layout

        checkPermission();
       // tabLayout = findViewById(R.id.tabs);//both tab and viewpager are reference from xml file
        //viewPager = findViewById(R.id.viewpager);


        nearByFragment= new NearByFragment();
       // PoiType poiType= (PoiType)((ObjectWrapperForBinder)getIntent().getExtras().getBinder("type").get);
        loadFragment(nearByFragment);

    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt= fm.beginTransaction();
        fmt.replace(R.id.tab_coordinator_layout, fragment);
        fmt.commit();
    }

    private void checkPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
