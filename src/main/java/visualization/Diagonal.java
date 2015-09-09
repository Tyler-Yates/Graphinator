package visualization;

/**
 * Represents the four diagonal directions.
 */
public enum Diagonal {
    UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT;

    /**
     * Returns the x value (cosine) of the given diagonal direction in a circle with the given
     * radius.
     *
     * @param diagonal the given diagonal direction
     * @param radius the given radius
     *
     * @return the x value
     */
    public static int getX(Diagonal diagonal, int radius) {
        return (int) (Math.cos(angleInRadians(diagonal)) * radius);
    }

    /**
     * Returns the y value (sine) of the given diagonal direction in a circle with the given
     * radius.
     *
     * @param diagonal the given diagonal direction
     * @param radius the given radius
     *
     * @return the y value
     */
    public static int getY(Diagonal diagonal, int radius) {
        return (int) (Math.sin(angleInRadians(diagonal)) * radius);
    }

    /**
     * Returns the angle in radians of the given diagonal direction.
     *
     * @param diagonal the given diagonal direction
     *
     * @return the angle in radians
     */
    public static double angleInRadians(Diagonal diagonal) {
        switch (diagonal) {
            case UPPER_LEFT:
                return 3.0 * Math.PI / 4.0;
            case UPPER_RIGHT:
                return Math.PI / 4.0;
            case LOWER_LEFT:
                return 5.0 * Math.PI / 4.0;
            case LOWER_RIGHT:
                return 7.0 * Math.PI / 4.0;
        }
        return 0.0;
    }
}
