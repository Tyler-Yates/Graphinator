package graph;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles visualizing edges of a graph.
 */
class ConnectionVisualizer {

    private final ConnectionManager manager;
    private final ConnectionCacher cacher = new ConnectionCacher();

    /**
     * Constructs a new visualizer that uses the given {@link graph.ConnectionManager} to gather
     * information about the graph's connections.
     *
     * @param connectionManager the connection manager for the graph
     */
    ConnectionVisualizer(ConnectionManager connectionManager) {
        manager = connectionManager;
    }

    /**
     * Draws the connections of a graph to a canvas.
     *
     * @param g the canvas
     */
    void drawConnections(Graphics g) {
        for (Connection connection : cacher.getConnections()) {
            connection.draw(g);
        }
    }

    /**
     * Caches connections to save the ConnectionManager from processing connections frequently.
     */
    class ConnectionCacher {
        private Set<Connection> cachedConnections = new HashSet<>();

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
