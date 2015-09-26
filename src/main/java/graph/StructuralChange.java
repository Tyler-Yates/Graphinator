package graph;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a structural change in the graph. This class is used by the {@link ColorManager} class
 * to recalculate coloring.
 */
public class StructuralChange {
    private final Set<Vertex> verticesChanged = new HashSet<>();

    /**
     * Creates a new structural change indicating the given vertices have been changed.
     *
     * @param vertices the given vertices
     */
    public StructuralChange(Vertex... vertices) {
        verticesChanged.addAll(Arrays.asList(vertices));
    }

    /**
     * Creates a new structural change indicating the given vertices have been changed.
     *
     * @param vertices the given vertices
     */
    public StructuralChange(Set<Vertex> vertices) {
        verticesChanged.addAll(vertices);
    }

    /**
     * Returns the vertices that were changed in the current structural change.
     *
     * @return the vertices that were changed in the current structural change
     */
    public Set<Vertex> getVerticesChanged() {
        return ImmutableSet.copyOf(verticesChanged);
    }
}
