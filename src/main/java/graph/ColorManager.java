package graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Manages the vertex colorings. Colors are assigned a unique ID that starts at zero.
 */
class ColorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ColorManager.class);

    private final Graph graph;

    private final Random generator = new Random();
    private final List<Color> colors = new ArrayList<>();

    private int maximumColor = -1;

    private static final double GOLDEN_RATIO_CONJUGATE = 0.618033988749895;

    /**
     * Constructs a new color manager for the given graph.
     *
     * @param graph the given graph
     */
    ColorManager(Graph graph) {
        this.graph = graph;
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(new Color(230, 127, 60));
        colors.add(Color.CYAN);
        colors.add(Color.PINK);
        colors.add(new Color(133, 35, 255));
        colors.add(new Color(162, 233, 76));
        colors.add(new Color(32, 77, 166));
    }

    /**
     * Returns the color associated with the given unique ID.
     *
     * @param colorID the unique color ID
     *
     * @return the color
     */
    Color getColor(int colorID) {
        while (colors.size() <= colorID) {
            colors.add(generateColor());
        }

        return colors.get(colorID);
    }

    private Color generateColor() {
        double HSB = generator.nextDouble();
        HSB += GOLDEN_RATIO_CONJUGATE;
        HSB %= 1;
        final int toRGB = Color.HSBtoRGB((float) HSB, (float) 0.99, (float) 0.99);
        return new Color(toRGB);
    }

    /**
     * Returns the number of colors present in the graph. Graphs with no vertices are defined to
     * have zero colors.
     *
     * @return the number of colors
     */
    int numberOfColors() {
        return maximumColor + 1;
    }

    /**
     * Assigns colors to all of the vertices in the graph.
     */
    void assignColors() {
        // Assume the graph is connected. This property will be checked in the process of
        // coloring vertices.
        graph.properties().graphIsConnected();
        if (graph.getVertexManager().numberOfVertices() == 0) {
            return;
        }

        LOGGER.debug("assigning colors-------------------------");

        // Reset the maximum color as we are reassigning all colors
        maximumColor = -1;
        final Iterator<Vertex> vertexIterator = graph.getVertexManager().getVertices().iterator();
        final Set<Vertex> verticesWithColorAssigned = new HashSet<>();

        // Start the color assignment from a random vertex in the graph
        processFromVertex(vertexIterator.next(), verticesWithColorAssigned);

        // If there are still vertices we have not assigned colors to then the graph is unconnected
        if (verticesWithColorAssigned.size() < graph.getVertexManager().numberOfVertices()) {
            graph.properties().graphIsDisconnected();
        }
        while (verticesWithColorAssigned.size() < graph.getVertexManager().numberOfVertices()) {
            final Vertex vertex = vertexIterator.next();
            if (!verticesWithColorAssigned.contains(vertex)) {
                processFromVertex(vertex, verticesWithColorAssigned);
            }
        }
    }

    private void processFromVertex(Vertex vertex, Set<Vertex> verticesWithColorAssigned) {
        final Queue<Vertex> next = new LinkedList<>();
        final Map<Vertex, Set<Integer>> forbiddenColors = new HashMap<>();

        // Add the first vertex to next
        next.add(vertex);

        while (!next.isEmpty()) {
            final Vertex current = next.poll();
            LOGGER.debug("current vertex: " + current);

            // Assign the first valid color to current
            final int assignedColor = pickFirstValidColor(forbiddenColors.get(current));
            if (assignedColor > maximumColor) {
                maximumColor = assignedColor;
            }
            current.setColor(assignedColor);
            LOGGER.debug("assigned color: " + assignedColor);

            verticesWithColorAssigned.add(current);

            final Set<Vertex> neighbors = current.getNeighbors();
            LOGGER.debug("checking neighbors: " + neighbors);
            for (Vertex neighbor : neighbors) {
                // Look only at vertices that have not yet been assigned a color
                if (!verticesWithColorAssigned.contains(neighbor)) {
                    LOGGER.debug("checking unassigned neighbor: " + neighbor);
                    final Set<Integer> forbidden = forbiddenColors.get(neighbor);
                    if (forbidden == null) {
                        final Set<Integer> newForbidden = new HashSet<>();
                        newForbidden.add(assignedColor);
                        forbiddenColors.put(neighbor, newForbidden);
                    } else {
                        forbidden.add(assignedColor);
                    }
                    LOGGER.debug("neighbor's forbidden colors: " + forbiddenColors.get(neighbor));

                    next.add(neighbor);
                } else {
                    LOGGER.debug("skipping assigned neighbor: " + neighbor);
                }
            }
        }
    }

    private int pickFirstValidColor(Set<Integer> forbiddenColors) {
        if (forbiddenColors == null) {
            return 0;
        }

        int color = 0;
        while (forbiddenColors.contains(color)) {
            color++;
        }
        return color;
    }
}
