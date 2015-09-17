package graph;

import visualization.Drawer;
import visualization.ScreenPosition;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Set;

/**
 * Represents a vertex in the graph.
 */
public class Vertex implements Comparable {
    private final int id;
    private final Graph graph;

    private int color = -1;
    private int x, y;
    private boolean selected;

    private static final int radius = 10;
    private static final int diameter = radius * 2;

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

    /**
     * Returns the ID of the current vertex.
     *
     * @return the ID
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the color of the current vertex.
     *
     * @return the color of the vertex
     */
    public int getColor() {
        return color;
    }

    /**
     * Uncolors the current vertex such that it has no color assigned to it.
     */
    public void uncolor() {
        color = -1;
    }

    /**
     * Returns the radius of the vertex.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Selects the given vertex
     */
    public void select() {
        selected = true;
    }

    /**
     * Deselects the given vertex
     */
    public void deselect() {
        selected = false;
    }

    /**
     * Returns whether the current vertex is selected.
     *
     * @return whether the current vertex is selected
     */
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

    /**
     * Returns the position of the current vertex on-screen accounting for the given canvas shift.
     *
     * @param canvasX the x canvas shift
     * @param canvasY the y canvas shift
     *
     * @return the on-screen position of the vertex
     */
    public ScreenPosition getScreenPosition(int canvasX, int canvasY) {
        return new ScreenPosition(x + canvasX, y + canvasY);
    }

    /**
     * Returns whether the given vertex is equal to the current vertex. Equality is based on the
     * unique identifier assigned to each vertex.
     *
     * @param other the given vertex
     *
     * @return whether the two vertices are equal
     */
    public boolean equals(Vertex other) {
        return getID() == other.getID();
    }

    /**
     * Adds a connection between this vertex and the given vertex.
     *
     * @param end the given vertex
     */
    public void addConnection(Vertex end) {
        graph.getConnectionManager().addConnection(this, end);
    }

    /**
     * Sets the color of the current vertex.
     *
     * @param color the color to set
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Returns the neighbors of the current vertex. The neighbors of a vertex are all the
     * vertices that are endpoints of connections originating from the vertex.
     *
     * @return the neighbors
     */
    public Set<Vertex> getNeighbors() {
        return graph.getConnectionManager().getNeighbors(this);
    }

    /**
     * Returns the x coordinate of the current vertex in the canvas.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the current vertex in the canvas.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Draws the current vertex to the given canvas taking into account the given canvas shift.
     *
     * @param g the canvas
     * @param cX the canvas shift in the x direction
     * @param cY the canvas shift in the y direction
     */
    public void draw(Graphics g, int cX, int cY) {
        // Determine which color we should draw the vertex with
        Color c = graph.getColorManager().getColor(color);
        if (c == null) {
            c = Drawer.getFrame().getBackground();
        }
        g.setColor(c);
        g.fillOval(x - radius + cX, y - radius + cY, diameter, diameter);

        // Highlight the edge of the vertex if it is selected
        if (selected) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.white);
        }
        g.drawOval(x - radius + cX, y - radius + cY, diameter, diameter);

        // Draw the ID
        g.setColor(getContrastColor(graph.getColorManager().getColor(color)));
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        drawStringCentered(g, Integer.toString(id), x - radius + cX, y - radius + cY, diameter,
                diameter);
    }

    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

    private static void drawStringCentered(Graphics g, String s, int x, int y, int width,
            int height) {
        final Rectangle stringBounds = getStringBounds((Graphics2D) g, s, x, y);
        final int stringWidth = (int) stringBounds.getWidth();
        final int stringHeight = (int) stringBounds.getHeight();
        g.drawString(s, x + (width / 2 - stringWidth / 2) - 1,
                y + (height / 2 + stringHeight / 2) + 1);
    }

    private static Rectangle getStringBounds(Graphics2D g2, String str, float x, float y) {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

    /**
     * Draws a ghost vertex to the given canvas at the given position
     *
     * @param g the canvas
     * @param x the x position to draw the vertex
     * @param y the y position to draw the vertex
     */
    public static void drawGhost(Graphics g, int x, int y) {
        g.setColor(new Color(128, 128, 128, 128));
        g.fillOval(x - radius, y - radius, diameter, diameter);
        g.setColor(new Color(255, 255, 255, 128));
        g.drawOval(x - radius, y - radius, diameter, diameter);
    }

    /**
     * Returns the degree of the vertex.
     *
     * @return the degree of the vertex
     */
    public int getDegree() {
        return graph.getConnectionManager().getNeighbors(this).size();
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

    @Override
    public int hashCode() {
        return id;
    }

    public String toString() {
        return "" + getID();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Vertex) {
            return Integer.compare(id, ((Vertex) o).id);
        }
        return -1;
    }
}