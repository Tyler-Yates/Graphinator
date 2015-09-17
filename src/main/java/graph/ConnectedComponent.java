package graph;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a single connected component which consists of all nodes that are connected to each
 * other.
 */
public class ConnectedComponent {
    private final Set<Vertex> vertexSet;
    private final List<Vertex> vertexList;
    private final int degree;

    private ConnectedComponent(Set<Vertex> vertices) {
        this.vertexList = new ArrayList<>();
        int degree = 0;
        for (final Vertex vertex : vertices) {
            degree = Math.max(degree, vertex.getDegree());
            vertexList.add(vertex);
        }
        this.degree = degree;

        this.vertexSet = vertices;
    }

    /**
     * Finds the connected component that the given vertex is in.
     *
     * @param start the given vertex
     *
     * @return the connected component the given vertex is in
     */
    public static ConnectedComponent find(Vertex start) {
        final Set<Vertex> vertices = new HashSet<>();
        calculateConnectedComponent(start, vertices);
        return new ConnectedComponent(vertices);
    }

    private static void calculateConnectedComponent(Vertex vertex, Set<Vertex> vertices) {
        vertices.add(vertex);

        for (final Vertex neighbor : vertex.getNeighbors()) {
            if (!vertices.contains(neighbor)) {
                calculateConnectedComponent(neighbor, vertices);
            }
        }
    }

    /**
     * Returns the vertices in the current connected component
     *
     * @return the vertices in the current connected component
     */
    public List<Vertex> getVertices() {
        return ImmutableList.copyOf(vertexList);
    }

    /**
     * Returns whether the given vertex is in the current connected component.
     *
     * @param vertex the given vertex
     *
     * @return whether the given vertex is in the current connected component
     */
    public boolean containsVertex(Vertex vertex) {
        return vertexSet.contains(vertex);
    }

    /**
     * Returns the degree of the current connected component.
     *
     * @return the degree of the current connected component
     */
    public int getDegree() {
        return degree;
    }
}
