package com.example.concordiaguide;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import Helpers.CampusBuilder;
import Helpers.GraphBuilder;
import Models.Campus;
import Models.Edge;
import Models.Floor;
import Models.Node;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class GraphBuilderTest {
    private CampusBuilder cb;
    private Campus sgw;
    private Campus loyola;
    private Floor floor;
    private IndoorNavigationActivity indoorNavigationActivity;
    private GraphBuilder graphBuilder;

    @Before
    public void setUp() throws Exception {
        cb = new CampusBuilder(null);
        sgw = cb.buildSGW();
        loyola = cb.buildLoyola();
        indoorNavigationActivity = Robolectric.buildActivity(IndoorNavigationActivity.class).create().resume().get();
        indoorNavigationActivity.setCampuses(sgw, loyola);

        floor = sgw.getBuilding("hall").getFloor(8);
        graphBuilder = new GraphBuilder(indoorNavigationActivity.getApplicationContext().getResources().getXml(floor.getFloorMap()), floor);
    }

    @Test
    public void verifyCreateGraph() {
        Graph<Node, Edge> graph = graphBuilder.createGraph(indoorNavigationActivity.getApplicationContext().getResources().getXml(floor.getFloorMap()));
        assertTrue(graph != null);
    }

    @Test
    public void verifyGetRoomNode() {
        Boolean flag = false;
        String room = "807";
        try {
            Node node = graphBuilder.getRoomNode(room);
            for(String name : node.getRooms()) {
                if(name.equals(room)) {
                    flag = true;
                }
            }
        } catch(GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
        }
        assertTrue(flag);
    }

    @Test
    public void verifyGetShortestPathTo() {
        Boolean flag = false;
        try {
            List<Edge> edgeList = graphBuilder.getShortestPathTo("807", false, GraphBuilder.Direction.DOWN);
            if(!edgeList.isEmpty()) {
                flag = true;
            }
        } catch(GraphBuilder.RoomNotExistsException e) {
            e.printStackTrace();
        }
        assertTrue(flag);
    }
}
