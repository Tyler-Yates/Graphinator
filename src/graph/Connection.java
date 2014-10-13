package graph;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Represents a connection between two vertices in a graph.
 */
public class Connection {

    private final Vertex start;
    private final Vertex end;
    private final Line2D line;

    private boolean selected;

    /**
     * Constructs a new connection between the given vertices.
     *
     * @param start the starting vertex
     * @param end the ending vertex
     */
    public Connection(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
        line = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Returns the starting vertex for the connection.
     *
     * @return the starting vertex
     */
    public Vertex getStart() {
        return start;
    }

    /**
     * Returns the ending vertex for the connection.
     *
     * @return the ending vertex
     */
    public Vertex getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Connection) {
            final Connection other = (Connection) o;
            return start.equals(other.start) && end.equals(other.end);
        }
        return false;
    }

    /**
     * Returns the distance between the given point and the line segment representing the
     * connection.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     *
     * @return the distance between the point and the line
     */
    public double distance(int x, int y) {
        return line.ptSegDist(x, y);
    }

    /**
     * Marks the connection as selected. This affects how the connection is drawn on-screen.
     */
    public void select() {
        selected = true;
    }

    /**
     * Marks the connection as unselected. This affects how the connection is drawn on-screen.
     */
    public void deselect() {
        selected = false;
    }

    /**
     * Draws the connection to the given canvas.
     *
     * @param g the canvas
     */
    public void draw(Graphics g) {
        if (selected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }

        g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
    }
}
