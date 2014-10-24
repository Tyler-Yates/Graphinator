package util;

/**
 * Represents a position.
 */
public abstract class Position {

    private int x, y;

    /**
     * Creates a position at the given x and y coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the current position.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the current position.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }
}
