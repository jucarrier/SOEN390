package Helpers;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import Models.Edge;
import Models.Floor;
import Models.GatewayNodes;
import Models.Node;

public class GraphBuilder {
    public enum Direction {
        UP, DOWN, OUTSIDE
    }

    //private String handicapped_source;  // example: "Elevator 1"
    //private String not_handicapped_source;  // example: "Stairs"
    private GatewayNodes gatewayNodes;
    private Graph<Node, Edge> floorGraph;

    //you can get the parser like so:
    //getResources().getXml(R.drawable.ic_hall_8)           <---- from within an activity, otherwise the getResource() method won't work
    public GraphBuilder(XmlPullParser parser, Floor floor) {
        this.floorGraph = createGraph(parser);
        this.gatewayNodes = floor.getGatewayNodes();
    }

    //return the list of edges from source room to target room
    public List<Edge> getShortestPath(String sourceRoom, String targetRoom) throws RoomNotExistsException {
        ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> paths = getShortestPathsGraph(sourceRoom);
        return paths.getPath(getRoomNode(targetRoom)).getEdgeList();
    }

    //returns the list of edges of the shortest path to the targetRoom with considerations if handicapped
    public List<Edge> getShortestPathTo(String targetRoom, boolean handicapped, Direction direction) throws RoomNotExistsException {
        ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> paths;

        //gets the graph with the all the shortest paths to all nodes from the sourceRoom
        if(handicapped && direction == Direction.UP) {
            paths = getShortestPathsGraph(gatewayNodes.getHandicappedUp());
        } else if(handicapped && direction == Direction.DOWN) {
            paths = getShortestPathsGraph(gatewayNodes.getHandicappedDown());
        } else if(!handicapped && direction == Direction.UP) {
            paths = getShortestPathsGraph(gatewayNodes.getNonHandicappedUp());
        } else if(!handicapped && direction == Direction.DOWN) {
            paths = getShortestPathsGraph(gatewayNodes.getNonHandicappedDown());
        } else if(direction == Direction.OUTSIDE) {
            paths = getShortestPathsGraph(gatewayNodes.getOutside());
        } else {
            paths = null;
        }

        return paths.getPath(getRoomNode(targetRoom)).getEdgeList();
    }

    //returns the list of edges of the shortest path from the sourceRoom with considerations if handicapped to the stairs/elevator/outside
    public List<Edge> getShortestPathFrom(String sourceRoom, boolean handicapped, Direction direction) throws RoomNotExistsException {
        ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> paths = getShortestPathsGraph(sourceRoom);

        //gets the graph with the all the shortest paths to all nodes from the sourceRoom
        if(handicapped && direction == Direction.UP) {
            return paths.getPath(getRoomNode(gatewayNodes.getHandicappedUp())).getEdgeList();
        } else if(handicapped && direction == Direction.DOWN) {
            return paths.getPath(getRoomNode(gatewayNodes.getHandicappedDown())).getEdgeList();
        } else if(!handicapped && direction == Direction.UP) {
            return paths.getPath(getRoomNode(gatewayNodes.getNonHandicappedUp())).getEdgeList();
        } else if(!handicapped && direction == Direction.DOWN) {
            return paths.getPath(getRoomNode(gatewayNodes.getNonHandicappedDown())).getEdgeList();
        } else if(direction == Direction.OUTSIDE) {
            return paths.getPath(getRoomNode(gatewayNodes.getOutside())).getEdgeList();
        } else {
            return null;
        }
    }

    //returns the graph with the all the shortest paths to all nodes from the sourceRoom
    public ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> getShortestPathsGraph(String room) throws RoomNotExistsException {
        DijkstraShortestPath<Node, Edge> dijkstraAlg = new DijkstraShortestPath<>(floorGraph);
        ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> paths = dijkstraAlg.getPaths(getRoomNode(room));
        return paths;
    }

    //get the node corresponding to the room given
    public Node getRoomNode(String room) throws RoomNotExistsException {
        for(Node node : floorGraph.vertexSet()) {
            for(String r : node.getRooms()) {
                if(r.equals(room)) {
                    return node;
                }
            }
        }

        throw new RoomNotExistsException("Room [ " + room + " ] does not exist.");
    }

    //returns the complete graph of the floor
    public Graph<Node, Edge> createGraph(XmlPullParser parser) {
        //initialize needed variables
        Graph<Node, Edge> gFloor = GraphTypeBuilder.<Node, Edge> undirected().allowingMultipleEdges(false).allowingSelfLoops(false).edgeClass(Edge.class).weighted(true).buildGraph();
        String[] elements, sourceRooms, targetRooms;
        Double weight = 200.0;
        Node sNode, tNode;
        Edge edge;
        //int nodeID = 0;
        String name, value;
        int count;

        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {

                //check if its a start tag
                if (parser.getEventType() == XmlPullParser.START_TAG) {

                    //get the count of attributes
                    count = parser.getAttributeCount();

                    //go through each attribute to find the name
                    for(int i = 0; i < count; i++) {
                        name = parser.getAttributeName(i);

                        //check if attribute is name
                        if(name.equals("name")) {

                            //get the value of the name attribute
                            value = parser.getAttributeValue(i);

                            //check if its an edge
                            if(value.matches("(^edge)(.*)")) {
                                // 0: "edge"    1: edge id    2: source rooms   3: target rooms     4: weight
                                elements = value.split("_");

                                //extract data
                                sourceRooms = elements[2].split("\\+");
                                targetRooms = elements[3].split("\\+");
                                weight = Double.parseDouble(elements[4]);

                                //create nodes and edges to add to the graph
                                sNode = new Node(sourceRooms);
                                tNode = new Node(targetRooms);
                                edge = new Edge(value);

                                //add data to graph
                                gFloor.addVertex(sNode);
                                gFloor.addVertex(tNode);
                                gFloor.addEdge(sNode, tNode, edge);
                                gFloor.setEdgeWeight(edge, weight);
                            }

                            //end the search for the name attribute
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gFloor;
    }

    public class RoomNotExistsException extends Throwable {
        public RoomNotExistsException(String errorMessage) {
            super(errorMessage);
        }
    }
}
