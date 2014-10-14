package graph;

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
    private final VertexManager vertexManager;

    private final Random generator = new Random();
    private static final double GOLDEN_RATIO_CONJUGATE = 0.618033988749895;
    private final List<Color> colors = new ArrayList<>();

    private int maximumColor = -1;

    /**
     * Constructs a new color manager with default colors.
     *
     * @param vertexManager the vertex manager
     */
    ColorManager(VertexManager vertexManager) {
        this.vertexManager = vertexManager;
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.ORANGE);
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
        // Reset the maximum color as we are reassigning all colors
        maximumColor = -1;
        final Iterator<Vertex> vertexIterator = vertexManager.getVertices().iterator();
        final Set<Vertex> verticesWithColorAssigned = new HashSet<>();

        // Start the color assignment from a random vertex in the graph
        processFromVertex(vertexIterator.next(), verticesWithColorAssigned);

        // If there are still vertices we have not assigned colors to then the graph is unconnected
        while (verticesWithColorAssigned.size() < vertexManager.numberOfVertices()) {
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

            // Assign the first valid color to current
            final int assignedColor = pickFirstValidColor(forbiddenColors.get(current));
            if(assignedColor > maximumColor) {
                maximumColor = assignedColor;
            }
            current.setColor(assignedColor);

            verticesWithColorAssigned.add(current);

            final Set<Vertex> neighbors = current.getNeighbors();
            for (Vertex neighbor : neighbors) {
                // Look only at vertices that have not yet been assigned a color
                if (!verticesWithColorAssigned.contains(neighbor)) {
                    final Set<Integer> forbidden = forbiddenColors.get(neighbor);
                    if (forbidden == null) {
                        final Set<Integer> newForbidden = new HashSet<>();
                        newForbidden.add(assignedColor);
                        forbiddenColors.put(neighbor, newForbidden);
                    } else {
                        forbidden.add(assignedColor);
                    }

                    next.add(neighbor);
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
