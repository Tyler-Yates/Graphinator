package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the vertices of a graph.
 */
class VertexManager {

    private final ConnectionManager connectionManager;

    private final Map<Integer, Vertex> vertices = new HashMap<>();
    private int nextVertexIDToAssign = 0;

    /**
     * Constructs a new vertex manager with the given connection manager.
     *
     * @param connectionManager the connection manager
     */
    VertexManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Returns the vertex with the given unique ID.
     *
     * @param ID the unique ID of the vertex
     *
     * @return the vertex
     */
    Vertex getVertex(int ID) {
        return vertices.get(ID);
    }

    /**
     * Adds a vertex to the graph at the given x and y coordinates.
     *
     * @param x the x coordinate of the vertex
     * @param y the y coordinate of the vertex
     *
     * @return the unique ID assigned to the vertex
     */
    int createVertex(int x, int y) {
        final int vertexID = nextVertexIDToAssign++;
        final Vertex newVertex = new Vertex(x, y, vertexID);
        vertices.put(vertexID, newVertex);
        return vertexID;
    }

    /**
     * Removes the vertex with the given ID from the graph. This also removes all edges
     * associated with that vertex in the graph.
     *
     * @param ID the unique ID of the vertex
     *
     * @return the removed vertex or {@code null} if no vertex with the given ID existed in the
     * graph
     */
    Vertex removeVertex(int ID) {
        final Vertex vertexToRemove = vertices.get(ID);
        if (vertexToRemove != null) {
            removeVertex(vertexToRemove);
        }
        return vertexToRemove;
    }

    /**
     * Removes the given vertex from the graph. This also removes all edges
     * associated with that vertex in the graph.
     *
     * @param vertexToRemove the vertex to remove
     */
    void removeVertex(Vertex vertexToRemove) {
        connectionManager.removeVertex(vertexToRemove);
        vertices.remove(vertexToRemove.getID());
    }

    /**
     * Returns the vertices of the graph. The ordering of the vertices is not guaranteed to be
     * consistent.
     *
     * @return the vertices of the graph
     */
    Collection<Vertex> getVertices() {
        return vertices.values();
    }

    /**
     * Returns the total number of vertices in the graph.
     *
     * @return the number of vertices
     */
    int numberOfVertices() {
        return vertices.size();
    }
}
