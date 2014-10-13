package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages the connections of a graph.
 */
class ConnectionManager {

    private Map<Vertex, Set<Vertex>> connections = new HashMap<Vertex, Set<Vertex>>();

    /**
     * Returns whether the given vertices are connected in the graph.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     * @return whether the two vertices are connected
     */
    public boolean verticesConnected(Vertex start, Vertex end) {
        final Set<Vertex> neighbors = connections.get(start);
        return neighbors != null && neighbors.contains(end);
    }

    /**
     * Adds the given connection to the graph. The connection is unidirectional such that {@code
     * start} may go to {@code end} but {@code end} may not go to {@code start}.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    public void addConnection(Vertex start, Vertex end) {
        Set<Vertex> neighbors = connections.get(start);
        if (neighbors == null) {
            neighbors = new HashSet<Vertex>();
            neighbors.add(end);
            connections.put(start, neighbors);
        } else {
            neighbors.add(end);
        }
    }

    /**
     * Removes the given connection from the graph if it exists.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    public void removeConnection(Vertex start, Vertex end) {
        final Set<Vertex> neighbors = connections.get(start);
        if (neighbors != null) {
            neighbors.remove(end);
        }
    }

    /**
     * Removes all connections both to and from the given vertex from the graph.
     *
     * @param removed the vertex to remove connections both from and to
     */
    public void removeVertex(Vertex removed) {
        // Remove all of the connections from the vertex
        connections.remove(removed);

        // Remove all connections going to the vertex
        for (Set<Vertex> neighbors : connections.values()) {
            neighbors.remove(removed);
        }
    }

    /**
     * Returns a set representing all connections in the graph.
     *
     * @return the set of all connections
     */
    public Set<Connection> getConnections() {
        final Set<Connection> connectionSet = new HashSet<Connection>();

        for (Map.Entry<Vertex, Set<Vertex>> entry : connections.entrySet()) {
            for (Vertex end : entry.getValue()) {
                final Vertex start = entry.getKey();
                connectionSet.add(new Connection(start, end));
            }
        }

        return connectionSet;
    }
}
