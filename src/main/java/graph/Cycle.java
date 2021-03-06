package graph;

import com.google.common.collect.ImmutableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a cycle in the graph. A cycle does not repeat vertices and starts and ends at the
 * same vertex.
 */
public class Cycle {
    private final ImmutableList<Vertex> vertices;
    private final Set<Vertex> vertexSet;

    /**
     * Constructs a cycle containing the given vertices
     *
     * @param vertices the given vertices
     */
    public Cycle(List<Vertex> vertices) {
        this.vertices = ImmutableList.copyOf(vertices);
        this.vertexSet = new HashSet<>(vertices);
    }

    /**
     * Returns the first vertex in the cycle.
     *
     * @return the first vertex in the cycle
     */
    public Vertex getFirstVertex() {
        return vertices.get(0);
    }

    /**
     * Returns an immutable view of the vertices in this cycle.
     *
     * @return the vertices in the cycle
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Returns the length of the cycle.
     *
     * @return the length of the cycle
     */
    public int length() {
        return vertices.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Cycle && ((Cycle) other).vertexSet.equals(vertexSet);
    }

    @Override
    public int hashCode() {
        return vertexSet.hashCode();
    }

    @Override
    public String toString() {
        return vertices.toString();
    }
}
