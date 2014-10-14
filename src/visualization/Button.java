package visualization;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Defines a visual element that performs an action when the user interacts with it.
 */
public abstract class Button {
    private int x, y;
    private int width, height;
    private String text;

    private ButtonState buttonState = ButtonState.NORMAL;

    private static final Font textFont = new Font("Arial", Font.BOLD, 14);

    /**
     * Constructs a new button at the given location with the given size.
     *
     * @param x the x position of the button
     * @param y the y position of the button
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param text the text of the button
     */
    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    /**
     * Sets the state of the current button.
     *
     * @param buttonState the button state
     */
    public void setButtonState(ButtonState buttonState) {
        this.buttonState = buttonState;
    }

    /**
     * Returns the current state of the button.
     *
     * @return the current button state
     */
    public ButtonState getButtonState() {
        return buttonState;
    }

    /**
     * Draws the visual element to the given graphics object.
     *
     * @param g the graphics object
     */
    void draw(Graphics g) {
        g.setFont(textFont);

        final Color backgroundColor;
        final Color textColor;
        switch (buttonState) {
            case NORMAL:
                backgroundColor = Color.LIGHT_GRAY;
                textColor = Color.BLACK;
                break;
            case HOVER:
                backgroundColor = new Color(127, 127, 127);
                textColor = Color.WHITE;
                break;
            case SELECTED:
                backgroundColor = Color.DARK_GRAY;
                textColor = Color.WHITE;
                break;
            default:
                backgroundColor = Color.LIGHT_GRAY;
                textColor = Color.BLACK;
        }

        g.setColor(backgroundColor);
        g.fillRect(x, y, width, height);

        if (buttonState == ButtonState.SELECTED) {
            g.setColor(Color.red);
            g.drawRect(x, y, width, height);
        }

        g.setColor(textColor);
        drawStringCentered(g, text, x - 5, width, y + height / 2, g.getColor());
    }

    /**
     * Returns whether the given coordinate is within the bounds of the button.
     *
     * @param cx the x position of the coordinate
     * @param cy the y position of the coordinate
     *
     * @return whether the coordinate is within the button
     */
    public boolean contains(int cx, int cy) {
        return cx > x && cx < x + width && cy > y && cy < y + height;
    }

    /**
     * Performs the action associated with the current button.
     */
    public abstract void perform() throws Exception;

    /**
     * Draws a given string centered x-wise at the given y value using the specified width.
     *
     * @param g the graphics object
     * @param s the string to draw
     * @param x the left-most x position of the box to draw the text in
     * @param width the width of the text box
     * @param y the y position of the text box
     * @param c the color to draw the text in
     */
    private static void drawStringCentered(Graphics g, String s, int x, int width, int y, Color c) {
        g.setColor(c);
        final Rectangle2D bds1 = g.getFont().createGlyphVector(((Graphics2D) g)
                .getFontRenderContext(), s).getVisualBounds();
        final int w = (int) bds1.getWidth();
        g.drawString(s, x + (width / 2 - w / 2), y);
    }
}
