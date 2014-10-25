package graph;

import visualization.Drawer;

import java.awt.*;
import java.io.Serializable;
import java.util.Set;

public class Vertex implements Serializable {
    private static final long serialVersionUID = 1122311460388446576L;
    private int color = 0;
    private int x, y;
    private final int id;
    private static final int radius = 10;
    private static final int diameter = radius * 2;

    private final Graph graph;

    private boolean selected = false;

    /**
     * Creates a new vertex with the given ID at the given position in the given graph.
     *
     * @param id the ID of the vertex
     * @param position the position of the vertex
     * @param graph the graph
     */
    Vertex(int id, CanvasPosition position, Graph graph) {
        this.id = id;
        x = position.getX();
        y = position.getY();
        this.graph = graph;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the position of the current vertex.
     *
     * @param position the new position of the vertex
     */
    public void setPosition(CanvasPosition position) {
        x = position.getX();
        y = position.getY();
    }

    public int getID() {
        return id;
    }

    public boolean equals(Vertex other) {
        return getID() == other.getID();
    }

    public void addConnection(Vertex end) {
        graph.getConnectionManager().addConnection(this, end);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int colorValue) {
        color = colorValue;
    }

    public Set<Vertex> getNeighbors() {
        return graph.getConnectionManager().getNeighbors(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g, int cX, int cY) {
        Color c = graph.getColorManager().getColor(color);
        if (c == null) {
            c = Drawer.getFrame().getBackground();
        }
        g.setColor(c);
        g.fillOval(x - radius + cX, y - radius + cY, diameter, diameter);

        if (selected) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.white);
        }
        g.drawOval(x - radius + cX, y - radius + cY, diameter, diameter);
    }

    /**
     * Returns the degree of the vertex.
     *
     * @return the degree of the vertex
     */
    public int getDegree() {
        return graph.getConnectionManager().getNeighbors(this).size();
    }

    public String toString() {
        return "" + getID();
    }

    /**
     * Removes the connection between the current vertex and the given vertex if it exists.
     *
     * @param other the given vertex
     */
    public void removeConnection(Vertex other) {
        graph.getConnectionManager().removeConnection(this, other);
    }

    /**
     * Returns whether the current vertex is a neighbor to the given vertex. If this method
     * returns true there is a connection from the given vertex to the current vertex.
     *
     * @param other the other vertex
     *
     * @return whether the current vertex is a neighbor to the given vertex
     */
    public boolean isNeighborTo(Vertex other) {
        return graph.getConnectionManager().verticesConnected(other, this);
    }

    /**
     * Returns whether the given vertex is a neighbor to the current vertex. If this method
     * returns true there is a connection from the current vertex to the other vertex.
     *
     * @param other the other vertex
     *
     * @return whether the other vertex is a neighbor to the current vertex
     */
    public boolean connectsTo(Vertex other) {
        return graph.getConnectionManager().verticesConnected(this, other);
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns the distance between the current vertex and the point represented by the given
     * position.
     *
     * @param position the given position
     *
     * @return the distance between this vertex and the given position
     */
    private double distance(CanvasPosition position) {
        final Point vertex = new Point(x, y);
        final Point mouse = new Point(position.getX(), position.getY());
        return vertex.distance(mouse);
    }

    /**
     * Returns whether the point represented by the given position lies withing the current
     * vertex.
     *
     * @param position the given position
     *
     * @return whether the position is within the vertex
     */
    public boolean pointInVertex(CanvasPosition position) {
        return distance(position) < radius;
    }
}