package Models;

/**
 * Represents one node that is used for indoor navigation. All navigation must be done from node
 * to node.
 */
public class Node {
    private final String[] rooms;

    public Node(String[] rooms) {
        this.rooms = rooms;
    }

    public String[] getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        String output = "";

        for (String room : rooms) {
            output += room + " ";
        }

        return output;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Node) && (toString().equals(o.toString()));
    }
}
