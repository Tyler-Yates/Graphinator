package graph;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Represents a connection between two vertices in a graph.
 */
public class Connection {

    private final Vertex start;
    private final Vertex end;

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
     * Returns the distance between the given position and the line segment representing the current
     * connection.
     *
     * @param position the position
     *
     * @return the distance between the position and the line
     */
    public double distance(CanvasPosition position) {
        final Line2D line = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
        return line.ptSegDist(position.getX(), position.getY());
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
     * @param canvasX the x coordinate of the canvas
     * @param canvasY the y coordinate of the canvas
     */
    public void draw(Graphics g, int canvasX, int canvasY) {
        if (selected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }

        g.drawLine(start.getX() + canvasX, start.getY() + canvasY, end.getX() + canvasX,
                end.getY() + canvasY);
    }

    public boolean isSelected() {
        return selected;
    }
}
