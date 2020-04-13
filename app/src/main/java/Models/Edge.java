package Models;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Represents an edge in the indoor navigation section.
 */
public class Edge extends DefaultWeightedEdge {
    private String edgeName;

    /**
     * @param edgeName Name is added because it is required for our functionality. The name
     *                 corresponds with the name in the SVG document.
     */
    public Edge(String edgeName) {
        super();
        this.edgeName = edgeName;
    }

    public String getEdgeName() {
        return edgeName;
    }

    @Override
    public String toString() {
        return edgeName;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Edge) && (toString().equals(o.toString()));
    }
}
