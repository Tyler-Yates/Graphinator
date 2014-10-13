package graph;

/**
 * Represents a connection between two vertices in a graph.
 */
public class Connection {

    private Vertex start;
    private Vertex end;

    /**
     * Constructs a new connection between the given vertices.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    public Connection(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the starting vertex for the connection.
     *
     * @return the starting vertex
     */
    public Vertex getStart() {
        return start;
    }

    /**
     * Returns the ending vertex for the connection.
     *
     * @return the ending vertex
     */
    public Vertex getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Connection) {
            final Connection other = (Connection) o;
            return start.equals(other.start) && end.equals(other.end);
        }
        return false;
    }

    //TODO add methods for distance and drawing
}
