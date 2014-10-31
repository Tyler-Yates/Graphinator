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

    private Vertex start;
    private Vertex end;

    @Before
    public void init() {
        Graph graph = new Graph();
        vertexManager = graph.getVertexManager();
        connectionManager = graph.getConnectionManager();

        start = createVertex();
        end = createVertex();
    }

    @Test
    public void testAddConnection() {
        // Connect start to end
        connectionManager.addConnection(start, end);
        assertEquals(1, connectionManager.numConnections());

        // There is a connection from start to end
        assertTrue(connectionManager.verticesConnected(start, end));
        // But NOT from end to start
        assertFalse(connectionManager.verticesConnected(end, start));
    }

    @Test
    public void testRemoveConnection() {
        // Connect start to end and end to start
        connectionManager.addConnection(start, end);
        connectionManager.addConnection(end, start);
        assertEquals(2, connectionManager.numConnections());
        assertTrue(connectionManager.verticesConnected(start, end));
        assertTrue(connectionManager.verticesConnected(end, start));

        // Now remove the connection from start to end
        connectionManager.removeConnection(start, end);
        // There should only remain a single connection from end to start
        assertEquals(1, connectionManager.numConnections());
        assertFalse(connectionManager.verticesConnected(start, end));
        assertTrue(connectionManager.verticesConnected(end, start));
    }

    @Test
    public void testToggleConnection() {
        connectionManager.addConnection(start, end);
        assertTrue(connectionManager.verticesConnected(start, end));

        // Vertices are connected so toggling should remove the connection
        connectionManager.toggleConnection(start, end);
        assertFalse(connectionManager.verticesConnected(start, end));

        // And toggling again should add the connection once more
        connectionManager.toggleConnection(start, end);
        assertTrue(connectionManager.verticesConnected(start, end));
    }

    private Vertex createVertex() {
        final int id = vertexManager.createVertex(new CanvasPosition(0, 0));
        return vertexManager.getVertex(id);
    }
}
