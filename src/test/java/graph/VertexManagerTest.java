package graph;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@SuppressWarnings("JavaDoc")
public class VertexManagerTest {

    private VertexManager vertexManager;
    private ConnectionManager connectionManager;

    @Before
    public void init() {
        Graph graph = new Graph();
        vertexManager = graph.getVertexManager();
        connectionManager = graph.getConnectionManager();
    }

    @Test
    public void testVertexCreationNoID() {
        final int vertexID1 = vertexManager.createVertex(new CanvasPosition(0, 0));
        final int vertexID2 = vertexManager.createVertex(new CanvasPosition(1, 1));

        // Assert that distinct IDs have been assigned
        assertNotEquals(vertexID1, vertexID2);
        // Assert that the vertices know their IDs
        assertEquals(vertexID1, vertexManager.getVertex(vertexID1).getID());
        assertEquals(vertexID2, vertexManager.getVertex(vertexID2).getID());
        // Assert that the correct number of vertices have been created
        assertEquals(2, vertexManager.numberOfVertices());
    }

    @Test
    public void testVertexCreationWithID() {
        final int vertexID1 = 0;
        final int vertexID2 = 1;

        // Assert that null is returned when the new vertices are created
        assertNull(vertexManager.createVertex(vertexID1, new CanvasPosition(0, 0)));
        assertNull(vertexManager.createVertex(vertexID2, new CanvasPosition(1, 1)));
        // Assert that the vertices know their IDs
        assertEquals(vertexID1, vertexManager.getVertex(vertexID1).getID());
        assertEquals(vertexID2, vertexManager.getVertex(vertexID2).getID());
        // Assert that the correct number of vertices have been created
        assertEquals(2, vertexManager.numberOfVertices());

        final Vertex vertex1 = vertexManager.getVertex(vertexID1);
        final Vertex oldVertex = vertexManager.createVertex(vertexID1, new CanvasPosition(2, 2));
        // Assert that the creation method returned the original vertex that was overwritten
        assertSame(vertex1, oldVertex);
        // Assert that the new vertex overwrote the original vertex
        assertNotSame(vertex1, vertexManager.getVertex(vertexID1));
        // Assert that the same number of vertices exist
        assertEquals(2, vertexManager.numberOfVertices());
    }

    @Test
    public void testRemoveVertex() {
        // Create three vertices
        final int id1 = vertexManager.createVertex(new CanvasPosition(0, 0));
        final int id2 = vertexManager.createVertex(new CanvasPosition(1, 1));
        final int id3 = vertexManager.createVertex(new CanvasPosition(2, 2));
        assertEquals(3, vertexManager.numberOfVertices());
        final Vertex vertex1 = vertexManager.getVertex(id1);
        final Vertex vertex2 = vertexManager.getVertex(id2);
        final Vertex vertex3 = vertexManager.getVertex(id3);

        // Create two connections between the vertices
        connectionManager.addConnection(vertex1, vertex2);
        connectionManager.addConnection(vertex3, vertex1);
        assertEquals(2, connectionManager.numConnections());

        // Remove the vertex that is an end of both connections
        vertexManager.removeVertex(vertex1);
        // There should now be only two vertices and one connection
        assertEquals(2, vertexManager.numberOfVertices());
        assertEquals(0, connectionManager.numConnections());
    }
}
