package visualization;

import graph.Vertex;

import java.awt.geom.Rectangle2D;

/**
 * Represents a rectangle on-screen. The class provides helper methods to map the rectangle onto
 * the canvas.
 */
public class RectangleOnScreen {
    private final Rectangle2D rectangle;

    /**
     * Constructs an on-screen rectangle whose upper-left corner is at the given screen position
     * and whose size is defined by the given width and height.
     *
     * @param upperLeft the upper-left corner of the rectangle on-screen
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public RectangleOnScreen(ScreenPosition upperLeft, int width, int height) {
        rectangle = new Rectangle2D.Double(upperLeft.getX(), upperLeft.getY(), width, height);
    }

    /**
     * Returns whether the current on-screen rectangle contains the given vertex taking into
     * account how much the canvas has shifted.
     *
     * @param vertex the given vertex
     * @param canvasX the x canvas shift
     * @param canvasY the y canvas shift
     *
     * @return whether the current on-screen rectangle contains the given vertex
     */
    public boolean containsVertex(Vertex vertex, int canvasX, int canvasY) {
        return new Rectangle2D.Double(rectangle.getX() - canvasX, rectangle.getY() - canvasY,
                rectangle.getWidth(), rectangle.getHeight()).contains(vertex.getX(), vertex.getY());
    }

    /**
     * Returns the on-screen x value.
     *
     * @return the on-screen x value
     */
    public int getX() {
        return (int) rectangle.getX();
    }

    /**
     * Returns the on-screen y value.
     *
     * @return the on-screen y value
     */
    public int getY() {
        return (int) rectangle.getY();
    }
}
