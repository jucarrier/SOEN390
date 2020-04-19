package com.example.concordiaguide;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
public class ShuttleTest {

    private Shuttle shuttle;

    @Before
    public void setUp() throws Exception {
        shuttle = Robolectric.buildActivity(Shuttle.class).create().resume().get();
    }

    @Test
    public void verifySGWclicked(){
        assertNull(shuttle.latlng[0]);
        Button goSGW = shuttle.findViewById(R.id.shuttle_goSGW);
        goSGW.performClick();
        assertEquals(shuttle.latlng[0].latitude, 45.458372, 0.0001);
    }

    @Test
    public void verifyLoyolaclicked(){
        assertNull(shuttle.latlng[0]);
        Button goSGW = shuttle.findViewById(R.id.shuttle_goLoyola);
        goSGW.performClick();
        assertEquals(shuttle.latlng[0].latitude, 45.497041, 0.0001);
    }

    @Test
    public void VerifygoToCampus(){
        LatLng from = new LatLng(45.458372, 45.454642);
        LatLng to = new LatLng(45.453456, 45.445442);
        shuttle.goToCampus(from, to);
        assert shuttle.bundle.getBoolean("active");
    }
}
