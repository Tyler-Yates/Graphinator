package graph;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("JavaDoc")
public class VertexTest {

    private Graph graph;
    private VertexManager vertexManager;
    private ConnectionManager connectionManager;
    private Vertex vertex1;
    private Vertex vertex2;
    private Vertex vertex3;
    private Vertex vertex4;

    @Before
    public void init() {
        graph = new Graph();
        vertexManager = graph.getVertexManager();
        connectionManager = graph.getConnectionManager();

        vertex1 = createVertex();
        vertex2 = createVertex();
        vertex3 = createVertex();
        vertex4 = createVertex();

        graph.addConnection(vertex1, vertex2);
        graph.addConnection(vertex2, vertex1);
        graph.addConnection(vertex1, vertex3);
        graph.addConnection(vertex3, vertex4);
    }

    @Test
    public void testAddConnection(@Mocked final ConnectionManager connectionManager) {
        new Expectations() {
            {
                connectionManager.addConnection(vertex1, vertex4);
                times = 1;
            }
        };

        vertex1.addConnection(vertex4);
    }

    @Test
    public void testGetNeighbors() {
        assertEquals(new HashSet<>(Arrays.asList(vertex2, vertex3)), vertex1.getNeighbors());
        assertEquals(new HashSet<>(Arrays.asList(vertex1)), vertex2.getNeighbors());
        assertEquals(new HashSet<>(Arrays.asList(vertex4)), vertex3.getNeighbors());
        assertEquals(Collections.<Vertex>emptySet(), vertex4.getNeighbors());
    }

    @Test
    public void testGetDegree() {
        assertEquals(2, vertex1.getDegree());
        assertEquals(1, vertex2.getDegree());
        assertEquals(1, vertex3.getDegree());
        assertEquals(0, vertex4.getDegree());

        connectionManager.removeConnection(vertex1, vertex2);
        assertEquals(1, vertex1.getDegree());
        assertEquals(1, vertex2.getDegree());
    }

    @Test
    public void testRemoveConnection(@Mocked final ConnectionManager connectionManager) {
        new Expectations() {
            {
                connectionManager.removeConnection(vertex1, vertex2);
                times = 1;
            }
        };

        vertex1.removeConnection(vertex2);
    }

    private Vertex createVertex() {
        final int id = vertexManager.createVertex(new CanvasPosition(0, 0));
        return vertexManager.getVertex(id);
    }
}