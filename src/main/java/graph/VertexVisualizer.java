package graph;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Handles visualizing vertices of a graph.
 */
class VertexVisualizer {

    private final VertexManager vertexManager;
    private final VertexCacher vertexCacher = new VertexCacher();

    VertexVisualizer(VertexManager vertexManager) {
        this.vertexManager = vertexManager;
    }

    void drawVertices(Graphics g, int canvasX, int canvasY) {
        for (Vertex vertex : vertexCacher.getVertices()) {
            vertex.draw(g, canvasX, canvasY);
        }
    }

    /**
     * Caches the vertices of a graph to save the vertex manager from processing vertices.
     */
    class VertexCacher {
        private Collection<Vertex> cachedVertices = new HashSet<>();

        /**
         * Returns the vertices of the graph. If the vertices have been changed the cache will be
         * updated to reflect those changes.
         *
         * @return the set of vertices
         */
        public Collection<Vertex> getVertices() {
            if (cachedVertices.size() != vertexManager.numberOfVertices()) {
                cachedVertices = vertexManager.getVertices();
            }
            return cachedVertices;
        }
    }
}
