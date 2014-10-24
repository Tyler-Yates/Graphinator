package visualization;

import util.MouseMode;

/**
 * Defines a button that sets a certain mode in the program.
 */
public class ModeButton extends Button {
    private MouseMode mode = MouseMode.VERTEX;

    /**
     * Creates a mode button at the given location with the given size and text.
     *
     * @param m the mode the button sets
     * @param screenPosition the position of the button
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param text the text of the button
     */
    public ModeButton(MouseMode m, ScreenPosition screenPosition, int width, int height,
            String text) {
        super(screenPosition, width, height, text);
        mode = m;
    }

    /**
     * Returns the mode the current button sets.
     *
     * @return the mode
     */
    public MouseMode getMode() {
        return mode;
    }

    @Override
    public void perform() throws Exception {
        Drawer.mode = mode;
    }
}