package Helpers;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import Models.Edge;
import Models.Floor;
import Models.Node;

public class GraphBuilder {
    private static final String edge = "EDGE";
    private static final String inode = "INODE";
    private static final String fnode = "FNODE";
    private static final String weight = "WEIGHT";

    private static XmlPullParser parser;

    public GraphBuilder() {
    }

    public List<Edge> getShortestPathEdgeListFor(Node target, ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> paths) {
        return paths.getPath(target).getEdgeList();
    }

    public ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> getShortestPathsGraph(String room, Graph<Node, Edge> graph) throws RoomNotExistsException {
        DijkstraShortestPath<Node, Edge> dijkstraAlg = new DijkstraShortestPath<>(graph);
        ShortestPathAlgorithm.SingleSourcePaths<Node, Edge> paths = dijkstraAlg.getPaths(getRoomNode(room, graph));
        return paths;
    }

    public Node getRoomNode(String room, Graph<Node, Edge> graph) throws RoomNotExistsException {
        for(Node node : graph.vertexSet()) {
            for(String r : node.getRooms()) {
                if(room.equals(room)) {
                    return node;
                }
            }
        }

        throw new RoomNotExistsException("Room [ " + room + " ] does not exist.");
    }

    //you can get the parser like so:
    //getResources().getXml(R.drawable.ic_hall_8)
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

    private class RoomNotExistsException extends Throwable {
        public RoomNotExistsException(String errorMessage) {
            super(errorMessage);
        }
    }
}
