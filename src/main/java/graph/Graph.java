package graph;

import java.awt.Graphics;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a graph including all the connections and vertices of the graph.
 */
public class Graph {
    private final ConnectionManager connectionManager = new ConnectionManager(this);
    private final ConnectionVisualizer connectionVisualizer = new ConnectionVisualizer(
            connectionManager);
    private final VertexManager vertexManager = new VertexManager(this);
    private final VertexVisualizer vertexVisualizer = new VertexVisualizer(vertexManager);
    private final ColorManager colorManager = new ColorManager(this);
    private final PropertyManager propertyManager = new PropertyManager(this);

    private final Queue<StructuralChange> structuralChanges = new LinkedList<>();

    private static final double REMOVAL_DISTANCE = 5.0;

    ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    VertexManager getVertexManager() {
        return vertexManager;
    }

    ColorManager getColorManager() {
        return colorManager;
    }

    /**
     * Returns the manager for the properties of the current graph.
     *
     * @return the properties manager for the current graph
     */
    public PropertyManager properties() {
        return propertyManager;
    }

    /**
     * Returns the vertices of the current graph.
     *
     * @return the vertices
     */
    public Collection<Vertex> getVertices() {
        return vertexManager.getVertices();
    }

    /**
     * Returns the vertex with the given ID or {@code null} if a vertex with the given ID is not
     * present in the graph.
     *
     * @param ID the unique ID of the vertex
     *
     * @return the vertex
     */
    public Vertex getVertex(int ID) {
        return vertexManager.getVertex(ID);
    }

    /**
     * Adds a vertex to the graph at the given position.
     *
     * @param position the the position of the vertex
     *
     * @return the unique ID assigned to the vertex
     */
    public int createVertex(CanvasPosition position) {
        return vertexManager.createVertex(position);
    }

    /**
     * Adds a vertex with the given unique ID to the graph at the given position.
     *
     * @param id the unique ID of the vertex
     * @param position the position of the vertex
     */
    public void createVertex(int id, CanvasPosition position) {
        vertexManager.createVertex(id, position);
    }

    /**
     * Removes the given vertex from the graph. This also removes all edges associated with that
     * vertex in the graph.
     *
     * @param vertexToRemove the vertex to remove
     */
    public void removeVertex(Vertex vertexToRemove) {
        vertexManager.removeVertex(vertexToRemove);
    }

    /**
     * Toggles the connection between the start and the end vertices such that if no connection
     * exists one will be created and if a connection does exist it will be removed.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    public void toggleConnection(Vertex start, Vertex end) {
        connectionManager.toggleConnection(start, end);
    }

    /**
     * Adds the given connection to the graph. The connection is unidirectional such that {@code
     * start} may go to {@code end} but {@code end} may not go to {@code start}.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    public void addConnection(Vertex start, Vertex end) {
        connectionManager.addConnection(start, end);
    }

    /**
     * Removes the given connection from the graph if it exists.
     *
     * @param connection the connection
     */
    public void removeConnection(Connection connection) {
        connectionManager.removeConnection(connection);
    }

    /**
     * Returns the total number of connections in the graph.
     *
     * @return the number of connections
     */
    public int numberOfConnections() {
        return connectionManager.numConnections();
    }

    /**
     * Returns the total number of vertices in the graph.
     *
     * @return the number of vertices
     */
    public int numberOfVertices() {
        return vertexManager.numberOfVertices();
    }

    /**
     * Returns the number of colors present in the graph. Graphs with no vertices are defined to
     * have zero colors.
     *
     * @return the number of colors
     */
    public int numberOfColors() {
        return colorManager.numberOfColors();
    }

    /**
     * Notify the graph that a structural change has occurred.
     *
     * @param verticesChanged the vertices that were changed
     */
    void structurallyChanged(Vertex... verticesChanged) {
        structuralChanges.add(new StructuralChange(verticesChanged));
    }

    /**
     * Notify the graph that a structural change has occurred.
     *
     * @param verticesChanged the vertices that were changed
     */
    void structurallyChanged(Set<Vertex> verticesChanged) {
        structuralChanges.add(new StructuralChange(verticesChanged));
    }

    /**
     * Checks whether the graph has been modified structurally and if so takes appropriate actions
     * to ensure that the graph properties are maintained.
     */
    private void checkForStructuralChanges() {
        // If the graph has been modified we need to update properties
        while (!structuralChanges.isEmpty()) {
            final StructuralChange structuralChange = structuralChanges.poll();

            // We need to reassign colors because the modified structure of the graph may cause a
            // new coloring
            colorManager.assignColors(structuralChange);
            propertyManager.calculateProperties();
        }
    }

    /**
     * Draws the current graph to the given canvas.
     *
     * @param g the canvas
     * @param canvasX the x position of the canvas
     * @param canvasY the y position of the canvas
     */
    public void drawGraph(Graphics g, int canvasX, int canvasY) {
        checkForStructuralChanges();

        connectionVisualizer.drawConnections(g, canvasX, canvasY);
        vertexVisualizer.drawVertices(g, canvasX, canvasY);
    }

    /**
     * Removes the highlight for all vertices and connections in the graph.
     */
    public void removeHighlights() {
        // Remove highlight from all of the vertices that are in range
        for (Vertex vertex : vertexManager.getVertices()) {
            vertex.deselect();
        }

        // Remove highlight from all of the connections that are in range
        for (Connection connection : connectionManager.getConnections()) {
            connection.deselect();
        }
    }

    /**
     * Marks all vertices and connections that are within range to be removed using the position of
     * the mouse.
     *
     * @param position the position of the mouse
     */
    public void highlightRemovals(CanvasPosition position) {
        // Highlight all of the vertices that are in range
        for (Vertex vertex : vertexManager.getVertices()) {
            if (vertex.pointInVertex(position)) {
                vertex.select();
                // Highlight all of the connections originating from the vertex because they will
                // be removed as well
                for (Connection connection : connectionManager.getVertexConnections(vertex)) {
                    connection.select();
                }
                // If the user removes a vertex we will remove all edges as well so don't worry
                // about checking them
                return;
            } else {
                vertex.deselect();
            }
        }

        //Highlight all of the connections that are in range
        for (Connection connection : connectionManager.getConnections()) {
            if (connection.distance(position) < REMOVAL_DISTANCE) {
                connection.select();
            } else {
                connection.deselect();
            }
        }
    }

    /**
     * Removes all vertices and connections that are within range to be removed using the position
     * of the mouse.
     *
     * @param position the position of the mouse
     */
    public void remove(CanvasPosition position) {
        // Highlight all of the vertices that are in range
        final Set<Vertex> verticesToRemove = new HashSet<>();
        for (Vertex vertex : vertexManager.getVertices()) {
            if (vertex.pointInVertex(position)) {
                verticesToRemove.add(vertex);
            }
        }
        for (Vertex vertex : verticesToRemove) {
            removeVertex(vertex);
        }

        //Highlight all of the connections that are in range
        final Set<Connection> connectionsToRemove = new HashSet<>();
        for (Connection connection : connectionManager.getConnections()) {
            if (connection.distance(position) < REMOVAL_DISTANCE) {
                connectionsToRemove.add(connection);
            }
        }
        for (Connection connection : connectionsToRemove) {
            removeConnection(connection);
        }
    }
}