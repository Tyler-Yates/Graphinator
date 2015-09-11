package visualization;

import util.Position;

/**
 * Represents a position on the screen. This relates to the resolution of the window displayed on
 * the user's monitor.
 */
public class ScreenPosition extends Position {

    /**
     * Creates a new screen position at the given coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public ScreenPosition(int x, int y) {
        super(x, y);
    }
}
