package graph;

import java.awt.*;
import java.util.Set;

/**
 * Handles visualizing edges of a graph.
 */
class ConnectionVisualizer {

    private final ConnectionManager manager;
    private final ConnectionCacher cacher;

    /**
     * Constructs a new visualizer that uses the given {@link graph.ConnectionManager} to gather
     * information about the graph's connections.
     *
     * @param connectionManager the connection manager for the graph
     */
    public ConnectionVisualizer(ConnectionManager connectionManager) {
        manager = connectionManager;
        cacher = new ConnectionCacher();
    }

    /**
     * Draws the connections of a graph to a canvas.
     *
     * @param g the canvas
     */
    public void drawConnections(Graphics g) {
        g.setColor(Color.WHITE);

        final Set<Connection> connections = cacher.getConnections();
        for (Connection connection : connections) {
            final Vertex start = connection.getStart();
            final Vertex end = connection.getEnd();
            g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }

    /**
     * Caches connections to save the ConnectionManager from processing connections frequently.
     */
    class ConnectionCacher {
        private Set<Connection> cachedConnections;

        /**
         * Returns the connections of the graph. If the connections have been changed the cache will
         * be updated to reflect those changes.
         *
         * @return the set of connections
         */
        public Set<Connection> getConnections() {
            if (cachedConnections.size() != manager.numConnections()) {
                cachedConnections = manager.getConnections();
            }

            return cachedConnections;
        }
    }
}
