package com.example.concordiaguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import Helpers.CampusBuilder;
import Helpers.ObjectWrapperForBinder;
import Models.Campus;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {

    private MainActivity mainActivity;
    private IndoorNavigationActivity indoorNavigationActivity;

    @Before
    public void setUp() throws Exception {
        CampusBuilder cb = new CampusBuilder(null);
        indoorNavigationActivity = Robolectric.buildActivity(IndoorNavigationActivity.class).create().resume().get();
        indoorNavigationActivity.setCampuses(cb.buildSGW(), cb.buildLoyola());
    }

    @Test
    public void verifyCampusOptions() {
        assertEquals(indoorNavigationActivity.getCampusSpinner().getAdapter().getCount(), 2);
    }
}