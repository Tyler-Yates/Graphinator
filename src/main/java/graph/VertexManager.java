package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the vertices of a graph.
 */
class VertexManager {

    private final Graph graph;

    private final Map<Integer, Vertex> vertices = new HashMap<>();
    private int nextVertexIDToAssign = 0;

    /**
     * Constructs a new vertex manager for the given graph.
     *
     * @param graph the given graph
     */
    VertexManager(Graph graph) {
        this.graph = graph;
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
        createVertex(vertexID, x, y);
        graph.structurallyChanged();
        return vertexID;
    }

    /**
     * Adds a vertex with the given unique ID to the graph at the given x and y coordinates.
     *
     * @param id the unique ID of the vertex
     * @param x the x coordinate of the vertex
     * @param y the y coordinate of the vertex
     */
    void createVertex(int id, int x, int y) {
        final Vertex newVertex = new Vertex(id, x, y, graph);
        graph.structurallyChanged();
        vertices.put(id, newVertex);
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
        graph.structurallyChanged();
        return vertexToRemove;
    }

    /**
     * Removes the given vertex from the graph. This also removes all edges
     * associated with that vertex in the graph.
     *
     * @param vertexToRemove the vertex to remove
     */
    void removeVertex(Vertex vertexToRemove) {
        graph.getConnectionManager().removeVertex(vertexToRemove);
        vertices.remove(vertexToRemove.getID());
        graph.structurallyChanged();
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
