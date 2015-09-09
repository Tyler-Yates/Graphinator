package graph;

import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 * Class used to construct a cycle.
 */
public class CycleBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(CycleBuilder.class);

    private final List<Vertex> vertices = new ArrayList<>();
    private final Set<Vertex> vertexSet = new HashSet<>();
    private boolean isCycle = false;

    /**
     * Attempts to add the given vertex to the cycle. If the vertex cannot be added because the
     * cycle properties would be violated, {@code false} is returned.
     *
     * @param vertex the given vertex
     *
     * @return whether the given vertex was successfully added to the cycle
     */
    public boolean addVertex(Vertex vertex) {
        LOGGER.debug("Attempting to add vertex " + vertex.getID());
        if (isCycle) {
            print("The cycle is already complete. Cannot add another vertex.");
            return false;
        }
        if (vertices.size() > 0) {
            if (!Iterables.getLast(vertices).getNeighbors().contains(vertex)) {
                print("Vertex " + vertex.getID() + " is not a neighbor of the last vertex in the " +
                        "cycle.");
                return false;
            }
        }

        if (vertexSet.contains(vertex)) {
            // We have come back to the beginning so the cycle is now complete
            if (vertex.equals(vertices.get(0)) && vertices.size() > 2) {
                isCycle = true;
                vertices.add(vertex);
            } else {
                print("Vertex " + vertex.getID() + " has been visited already and is not the " +
                        "first vertex in the cycle.");
                return false;
            }
        } else {
            vertices.add(vertex);
            vertexSet.add(vertex);
        }

        return true;
    }

    private void print(String message) {
        LOGGER.info(vertices.toString() + " - " + message);
    }

    /**
     * Removes the last vertex in the cycle.
     */
    public void removeVertex() {
        checkState(!vertices.isEmpty());
        final Vertex lastVertex = vertices.remove(vertices.size() - 1);
        if (!vertices.isEmpty() && !lastVertex.equals(vertices.get(0))) {
            vertexSet.remove(lastVertex);
        }
        isCycle = false;
    }

    /**
     * Returns whether the current builder can build a complete, valid cycle.
     *
     * @return whether the current builder can build a complete, valid cycle
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * Builds and returns the cycle if possible.
     *
     * @return the cycle
     *
     * @throws IllegalStateException if the cycle is not valid
     */
    public Cycle build() {
        checkState(isCycle);
        return new Cycle(vertices);
    }
}
