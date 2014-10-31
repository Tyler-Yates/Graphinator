package graph;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("JavaDoc")
public class ConnectionManagerTest {

    private VertexManager vertexManager;
    private ConnectionManager connectionManager;

    @Before
    public void init() {
        Graph graph = new Graph();
        vertexManager = graph.getVertexManager();
        connectionManager = graph.getConnectionManager();
    }

    @Test
    public void testAddConnection() {
        final Vertex start = createVertex();
        final Vertex end = createVertex();

        // Connect start to end
        connectionManager.addConnection(start, end);
        assertEquals(1, connectionManager.numConnections());

        // There is a connection from start to end
        assertTrue(connectionManager.verticesConnected(start, end));
        // But NOT from end to start
        assertFalse(connectionManager.verticesConnected(end, start));
    }

    private Vertex createVertex() {
        final int id = vertexManager.createVertex(new CanvasPosition(0, 0));
        return vertexManager.getVertex(id);
    }
}
