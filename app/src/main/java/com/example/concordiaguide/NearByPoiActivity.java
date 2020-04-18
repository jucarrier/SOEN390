package com.example.concordiaguide;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import Helpers.PoiFragment;

public class NearByPoiActivity extends AppCompatActivity {
    public PoiFragment poiFragment;

    // Bundle bundle= new Bundle(); q
    //Intent intent= new Intent();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_poi_activity);

        poiFragment = new PoiFragment();
        //PoiType pt= (PoiType)((ObjectWrapperForBinder)getIntent().getExtras().getBinder("poitype")).getData();
        //String type_PoiString= pt.getPoiType();
        //bundle.putString("poitype",type_PoiString);
        loadFragment(poiFragment);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        // fragment.setArguments(bundle);
        fmt.replace(R.id.nearby_poi_layout, fragment);
        fmt.commit();
    }

    public void onBackPressed() {

        //Intent intent2 = new Intent(this, MainActivity.class);
        //intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       //startActivity(intent2);
        // finish();
        super.onBackPressed();
    }
}
