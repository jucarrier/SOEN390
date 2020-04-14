package com.example.concordiaguide;

import android.graphics.Color;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.devs.vectorchildfinder.VectorDrawableCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import Helpers.CampusBuilder;
import Models.Building;
import Models.Campus;
import Models.Floor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class IndoorNavigationTest {

    private CampusBuilder cb;
    private Campus sgw;
    private Campus layola;
    private IndoorNavigationActivity indoorNavigationActivity;

    @Before
    public void setUp() throws Exception {
        cb = new CampusBuilder(null);
        sgw = cb.buildSGW();
        layola = cb.buildLoyola();
        indoorNavigationActivity = Robolectric.buildActivity(IndoorNavigationActivity.class).create().resume().get();
        indoorNavigationActivity.setCampuses(sgw, layola);
    }

    @Test
    public void verifyCampusOptions() {
        Spinner campusSpinner = indoorNavigationActivity.getCampusSpinner();
        assertEquals(campusSpinner.getAdapter().getCount(), 2);
        campusSpinner.setSelection(0);
        Spinner buildingSinner = indoorNavigationActivity.getBuildingSpinner();
        assertEquals(buildingSinner.getAdapter().getCount(), 9);
        buildingSinner.setSelection(0);
        Spinner floorSpinner = indoorNavigationActivity.getFloorSpinner();
        assertTrue(floorSpinner.getAdapter().getCount() >= 2);
        floorSpinner.setSelection(0);
        AutoCompleteTextView roomInput = indoorNavigationActivity.getRoomInput();
        assertTrue(roomInput.getAdapter().getCount() >= 2);
        roomInput.setText(roomInput.getAdapter().getItem(0).toString());
        assertTrue(roomInput.getText().toString().startsWith(buildingSinner.getSelectedItem().toString().substring(0, 1)));
    }

    @Test
    public void verifyRoomHighlight() {
        Building hall = sgw.getBuilding("Hall");
        Floor eightFloor = hall.getFloor(8);
        VectorDrawableCompat.VFullPath room;

        room = indoorNavigationActivity.highlightPathToRoom("H820", eightFloor, false, hall);
        assertEquals(room.getFillColor(), Color.BLUE);
        room = indoorNavigationActivity.highlightPathToRoom("801", eightFloor, false, hall);
        assertEquals(room.getFillColor(), Color.BLUE);
    }

    @Test
    public void verifyRoomPathing() {
        final String EDGE_NAME = "edge_35_823_820_50";
        Building hall = sgw.getBuilding("Hall");
        Floor eightFloor = hall.getFloor(8);
        VectorDrawableCompat.VFullPath edge = indoorNavigationActivity.highlightPathToRoom("H820", eightFloor, false, hall);
        assertEquals(edge.getStrokeColor(), Color.BLUE);
        assertEquals(edge.getPathName(), EDGE_NAME);
        edge = indoorNavigationActivity.highlightPathToRoom("820", eightFloor, false, hall);
        assertEquals(edge.getStrokeColor(), Color.BLUE);
        assertEquals(edge.getPathName(), EDGE_NAME);
        edge = indoorNavigationActivity.highlightPathToRoom("H820", eightFloor, true, hall);
        assertEquals(edge.getStrokeColor(), Color.BLUE);
        assertEquals(edge.getPathName(), EDGE_NAME);
        edge = indoorNavigationActivity.highlightPathToRoom("tree", eightFloor, true, hall);
        assertNull(edge);
    }
}