package com.example.concordiaguide;
import android.widget.Spinner;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import java.util.List;
import Helpers.CampusBuilder;
import Helpers.GraphBuilder;
import Models.Building;
import Models.Campus;
import Models.Edge;
import Models.Floor;

import static org.junit.Assert.assertEquals;
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
    private Campus loyola;
    private IndoorNavigationActivity indoorNavigationActivity;

    private final String TAG = "IndoorNavigationTest";

    @Before
    public void setUp() throws Exception {
        cb = new CampusBuilder(null);
        sgw = cb.buildSGW();
        loyola = cb.buildLoyola();
        indoorNavigationActivity = Robolectric.buildActivity(IndoorNavigationActivity.class).create().resume().get();
        indoorNavigationActivity.setCampuses(sgw, loyola);
    }

    @Test
    public void verifyCampusOptions() {
        Spinner campusSpinner = indoorNavigationActivity.getCampusSpinnerFrom();
        assertEquals(campusSpinner.getAdapter().getCount(), 2);
        campusSpinner.setSelection(0);
        Spinner buildingSinner = indoorNavigationActivity.getBuildingSpinnerFrom();
        assertEquals(buildingSinner.getAdapter().getCount(), 9);
        buildingSinner.setSelection(0);
        Spinner floorSpinner = indoorNavigationActivity.getFloorSpinnerFrom();
        assertTrue(floorSpinner.getAdapter().getCount() >= 2);
        floorSpinner.setSelection(0);
    }

    @Test
    public void verifyIndoorNavigationAlgorithm() {
        Building sourceBuilding = sgw.getBuilding("Hall");
        Floor sourceFloor = sourceBuilding.getFloor(8);
        String sourceRoom = "H807";

        //Building targetBuilding = sgw.getBuilding("Hall");
        //Floor targetFloor = targetBuilding.getFloor(8);
        String targetRoom = "H847";

        indoorNavigationActivity.setSource(sourceBuilding, sourceFloor, sourceRoom);
        indoorNavigationActivity.setTarget(sourceBuilding, sourceFloor, targetRoom);
        indoorNavigationActivity.setHandicapped(false);

        indoorNavigationActivity.showDirections();

        VectorChildFinder vector = new VectorChildFinder(indoorNavigationActivity, sourceFloor.getFloorMap(), indoorNavigationActivity.getImageView());
        GraphBuilder gb = new GraphBuilder(indoorNavigationActivity.getResources().getXml(sourceFloor.getFloorMap()), sourceFloor);
        VectorDrawableCompat.VFullPath edge;

        try {
            List<Edge> edges = gb.getShortestPath(sourceRoom.substring(1), targetRoom.substring(1));

            //check edges from map
            String room = sourceRoom;
            for(Edge e : edges) {
                edge = vector.findPathByName(e.getEdgeName());
                if (edge != null) {
                    room = e.getEdgeName();
                }
            }
            assertTrue(room.contains(targetRoom.substring(1)));

            //fill room color
            edge = vector.findPathByName(sourceRoom);
            if (edge != null) {
                assertTrue(edge.getPathName().contains(sourceRoom));
            }

            edge = vector.findPathByName(targetRoom);
            if (edge != null) {
                assertTrue(edge.getPathName().contains(targetRoom));
            }
        } catch (GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
            assertTrue(false);

        }
    }
}