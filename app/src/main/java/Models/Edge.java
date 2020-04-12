package Models;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    private String edgeName;

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
