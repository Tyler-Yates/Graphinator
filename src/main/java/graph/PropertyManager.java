package graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Used to detect and report the properties of a graph.
 */
public class PropertyManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyManager.class);

    private final Graph graph;

    private int maxDegree = 0;
    private boolean bipartite = false;
    private boolean tree = false;
    private boolean connected = true;
    private boolean regular;
    private int girth = 0;
    private final Set<Cycle> cycles = new HashSet<>();

    /**
     * Constructs a property manager for the given graph.
     *
     * @param graph the given graph
     */
    public PropertyManager(Graph graph) {
        this.graph = graph;
    }

    void graphIsConnected() {
        connected = true;
    }

    void graphIsDisconnected() {
        connected = false;
    }

    private void calculateMaxDegree() {
        int max = 0;
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.getDegree() > max) {
                max = vertex.getDegree();
            }
        }

        maxDegree = max;
    }

    /**
     * Returns the maximum degree of any of the vertices of the graph.
     *
     * @return the maximum degree
     */
    public int getMaxDegree() {
        return maxDegree;
    }

    private void calculateBipartite() {
        bipartite = graph.numberOfColors() <= 2;
    }

    /**
     * Returns whether the graph is bipartite.
     *
     * @return whether the graph is bipartite
     */
    public boolean isBipartite() {
        return bipartite;
    }

    /**
     * Returns whether the graph is connected.
     *
     * @return whether the graph is connected
     */
    public boolean isConnected() {
        return connected;
    }

    private void calculateTree() {
        // TODO fix definition for directed graphs
        tree = isConnected() && graph.numberOfConnections() / 2 == graph.numberOfVertices() - 1;
    }

    /**
     * Returns whether the graph is a tree.
     *
     * @return whether the graph is a tree
     */
    public boolean isTree() {
        return tree;
    }

    private void calculateRegularity() {
        if (graph.numberOfVertices() > 0) {
            final Iterator<Vertex> iterator = graph.getVertices().iterator();
            final int degree = iterator.next().getDegree();
            while (iterator.hasNext()) {
                if (iterator.next().getDegree() != degree) {
                    regular = false;
                    return;
                }
            }
        }
        regular = true;
    }

    /**
     * Returns whether the graph is regular.
     *
     * @return whether the graph is regular
     */
    public boolean isRegular() {
        return regular;
    }

    /**
     * Returns whether the graph is complete.
     *
     * @return whether the graph is complete
     */
    public boolean isComplete() {
        return maxDegree == graph.numberOfVertices() - 1 && isRegular();
    }

    /**
     * Returns the number of cycles in the graph.
     *
     * @return the number of cycles in the graph
     */
    public int numCycles() {
        return cycles.size();
    }

    /**
     * Returns the girth of the graph.
     *
     * @return the girth of the graph
     */
    public int getGirth() {
        return girth;
    }

    public void calculateCycles() {
        cycles.clear();
        girth = 0;

        for (final Vertex startingVertex : graph.getVertices()) {
            calculateCycle(startingVertex, new CycleBuilder());
        }
        LOGGER.info("Cycles: " + cycles.toString());
    }

    private void calculateCycle(Vertex currentVertex, CycleBuilder cycleBuilder) {
        if (cycleBuilder.addVertex(currentVertex)) {
            if (cycleBuilder.isCycle()) {
                final Cycle cycle = cycleBuilder.build();
                if (cycles.add(cycle)) {
                    if (girth == 0 || cycle.length() < girth) {
                        girth = cycle.length();
                    }
                }
            } else {
                for (final Vertex neighbor : currentVertex.getNeighbors()) {
                    calculateCycle(neighbor, cycleBuilder);
                }
            }
            cycleBuilder.removeVertex();
        }
    }

    void calculateProperties() {
        calculateMaxDegree();
        calculateTree();
        calculateBipartite();
        calculateRegularity();
        calculateCycles();
    }
}
