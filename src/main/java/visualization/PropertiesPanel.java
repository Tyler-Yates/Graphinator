package visualization;

import graph.Graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Responsible for drawing the properties of the current graph.
 */
public class PropertiesPanel {
    private final Graph graph;
    private final Map<String, Callable> properties = new HashMap<>();

    private final int width = 200;
    private int height = 20;

    /**
     * Constructs a properties panel for the given graph
     *
     * @param graph the given graph
     */
    public PropertiesPanel(Graph graph) {
        this.graph = graph;
        initializeProperties();
    }

    private void initializeProperties() {
        height = 20;

        addProperty("Number of Vertices: ", graph::numberOfVertices);
        //TODO For directed graphs don't divide by two
        addProperty("Number of Connections: ", () -> graph.numberOfConnections() / 2);
        addProperty("Number of Colors: ", graph::numberOfColors);
        addProperty("Maximum Degree: ", () -> graph.properties().getMaxDegree());
        addProperty("Bipartite: ", () -> graph.properties().isBipartite());
        addProperty("Connected: ", () -> graph.properties().isConnected());
        addProperty("Tree: ", () -> graph.properties().isTree());
        addProperty("Regular: ", () -> graph.properties().isRegular());
        addProperty("Complete: ", () -> graph.properties().isComplete());
        addProperty("Number of Cycles: ", () -> graph.properties().numCycles());
        addProperty("Girth: ", () -> graph.properties().getGirth());
    }

    private void addProperty(String description, Callable callable) {
        height += 20;
        properties.put(description, callable);
    }

    /**
     * Draws the properties panel to the given canvas
     *
     * @param g the canvas
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        int y = 20;
        for (Map.Entry<String, Callable> property : properties.entrySet()) {
            try {
                g.drawString(property.getKey() + property.getValue().call().toString(), 10, y);
            } catch (Exception e) {
                e.printStackTrace();
            }
            y += 20;
        }

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, height, width, height);
        g.drawLine(width, 0, width, height);
    }

    /**
     * Returns the rectangle that the properties panel draws on.
     *
     * @return the rectangle
     */
    public Rectangle getDrawingRectangle() {
        return new Rectangle(0, 0, width, height);
    }
}
