package graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages the connections of a graph.
 */
class ConnectionManager {

    private final Graph graph;
    private final Set<Connection> connections = new HashSet<>();

    /**
     * Constructs a new connection manager for the given graph.
     *
     * @param graph the given graph
     */
    ConnectionManager(Graph graph) {
        this.graph = graph;
    }

    /**
     * Returns whether the given vertices are connected in the graph.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     *
     * @return whether the two vertices are connected
     */
    boolean verticesConnected(Vertex start, Vertex end) {
        return connections.contains(new Connection(start, end));
    }

    /**
     * Toggles the connection between the start and the end vertices such that if no connection
     * exists one will be created and if a connection does exist it will be removed.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    void toggleConnection(Vertex start, Vertex end) {
        if (verticesConnected(start, end)) {
            removeConnection(start, end);
        } else {
            addConnection(start, end);
        }

        graph.structurallyChanged();
    }

    /**
     * Adds the given connection to the graph. The connection is unidirectional such that {@code
     * start} may go to {@code end} but {@code end} may not go to {@code start}.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    void addConnection(Vertex start, Vertex end) {
        connections.add(new Connection(start, end));

        graph.structurallyChanged();
    }

    /**
     * Removes the given connection from the graph if it exists.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    void removeConnection(Vertex start, Vertex end) {
        removeConnection(new Connection(start, end));
    }

    /**
     * Removes the given connection from the graph if it exists.
     *
     * @param connection the connection
     */
    void removeConnection(Connection connection) {
        connections.remove(connection);
        graph.structurallyChanged();
    }

    /**
     * Removes all connections both to and from the given vertex from the graph.
     *
     * @param removed the vertex to remove connections both from and to
     */
    void removeVertex(Vertex removed) {
        // Enumerate the connections to remove to prevent concurrent modification
        final Set<Connection> connectionsToRemove = new HashSet<>();
        for (Connection connection : connections) {
            if (connection.getStart().equals(removed) || connection.getEnd().equals(removed)) {
                connectionsToRemove.add(connection);
            }
        }
        for (Connection connection : connectionsToRemove) {
            connections.remove(connection);
        }

        graph.structurallyChanged();
    }

    /**
     * Returns a set representing all connections in the graph.
     *
     * @return the set of all connections
     */
    Set<Connection> getConnections() {
        return connections;
    }

    /**
     * Returns the neighbors of the given vertex. If the vertex has no neighbors an empty set will
     * be returned. The neighbors of a vertex are all the vertices that are endpoints of
     * connections originating from the vertex.
     *
     * @param start the starting vertex
     *
     * @return the neighbors
     */
    Set<Vertex> getNeighbors(Vertex start) {
        final Set<Vertex> neighbors = new HashSet<>();
        for (Connection connection : connections) {
            if (connection.getStart().equals(start)) {
                neighbors.add(connection.getEnd());
            }
        }

        return neighbors;
    }

    /**
     * Returns the connections that involve the given vertex either as the starting vertex of the
     * connection or the ending vertex of the connection.
     *
     * @param vertex the vertex
     *
     * @return the connections of the vertex
     */
    Set<Connection> getVertexConnections(Vertex vertex) {
        final Set<Connection> neighborConnections = new HashSet<>();
        for (Connection connection : connections) {
            if (connection.getStart().equals(vertex) || connection.getEnd().equals(vertex)) {
                neighborConnections.add(connection);
            }
        }

        return neighborConnections;
    }

    /**
     * Returns the number of connections for the given graph.
     *
     * @return the number of connections
     */
    int numConnections() {
        return connections.size();
    }
}
