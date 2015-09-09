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
     * Returns an immutable view of the vertices in this cycle.
     *
     * @return the vertices in the cycle
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Cycle && ((Cycle) other).vertexSet.equals(vertexSet);
    }

    @Override
    public int hashCode() {
        return vertices.hashCode();
    }
}
