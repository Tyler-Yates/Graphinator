package graph;

import util.Position;

/**
 * Represents a position in the canvas. This can be viewed as the "real world" coordinates of the
 * graph.
 */
public class CanvasPosition extends Position {

    /**
     * Constructs a new canvas position at the given coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public CanvasPosition(int x, int y) {
        super(x, y);
    }
}
