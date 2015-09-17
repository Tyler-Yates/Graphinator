package graph;

import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        if (colorID < 0) {
            return Color.BLACK;
        }

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

    private void resetColors() {
        setAllColors(-1);
    }

    private void setAllColors(int color) {
        for (final Vertex vertex : graph.getVertices()) {
            vertex.setColor(color);
        }
    }

    /**
     * Assigns colors to all of the vertices in the graph.
     */
    void assignColors() {
        maximumColor = 0;

        if (graph.numberOfConnections() == 0) {
            setAllColors(0);
            return;
        }

        resetColors();
        final Runnable colorRunnable = () -> {
            System.out.println("Calculating permutations");
            final Collection<List<Vertex>> vertexPermutations = Collections2.orderedPermutations(
                    graph.getVertices());

            int smallestColoring = Integer.MAX_VALUE;
            ColorMap smallestColoringMap = null;
            for (final List<Vertex> vertexOrdering : vertexPermutations) {
                final ColorMap colorMap = getColorMap(vertexOrdering);
                if (colorMap.getMaxColor() < smallestColoring) {
                    smallestColoring = colorMap.getMaxColor();
                    smallestColoringMap = colorMap;
                }
            }

            if (smallestColoringMap != null) {
                LOGGER.info("Optimal coloring found.");
                smallestColoringMap.assignColors();
                maximumColor = smallestColoringMap.getMaxColor();
            }
        };

        final Thread colorThread = new Thread(colorRunnable);
        colorThread.start();
    }

    private ColorMap getColorMap(List<Vertex> vertexOrdering) {
        final ColorMap colorMap = new ColorMap();

        for (final Vertex vertex : vertexOrdering) {
            final Set<Integer> neighborColors = new HashSet<>();
            for (final Vertex neighbor : vertex.getNeighbors()) {
                final Integer neighborColor = colorMap.getColor(neighbor);
                if (neighborColor != null) {
                    neighborColors.add(neighborColor);
                }
            }

            int color = 0;
            while (neighborColors.contains(color)) {
                color++;
            }
            colorMap.setColor(vertex, color);
        }

        return colorMap;
    }

    class ColorMap {
        int maxColor = 0;
        final Map<Vertex, Integer> colorMapping = new HashMap<>();

        public Integer getColor(Vertex v) {
            return colorMapping.get(v);
        }

        public void setColor(Vertex v, int color) {
            colorMapping.put(v, color);
            if (color > maxColor) {
                maxColor = color;
            }
        }

        public void assignColors() {
            for (final Map.Entry<Vertex, Integer> vertexAndColor : colorMapping.entrySet()) {
                vertexAndColor.getKey().setColor(vertexAndColor.getValue());
            }
        }

        public int getMaxColor() {
            return maxColor;
        }
    }
}
