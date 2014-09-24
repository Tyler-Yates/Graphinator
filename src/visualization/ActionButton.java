package visualization;

import util.Action;
import util.FileOperations;

/**
 * Defines a button that performs an immediate action.
 */
public class ActionButton extends Button {

    private Action action;

    /**
     * Constructs a new button at the given location with the given size.
     *
     * @param x      the x position of the button
     * @param y      the y position of the button
     * @param width  the width of the button in pixels
     * @param height the height of the button in pixels
     * @param text   the text of the button
     * @param action the action the button will perform
     */
    public ActionButton(int x, int y, int width, int height, String text, Action action) {
        super(x, y, width, height, text);
        this.action = action;
    }

    @Override
    public void perform() throws Exception {
        switch (action) {
            case SAVE:
                FileOperations.saveFile();
                break;
            case LOAD:
                FileOperations.loadFile();
                break;
        }
    }
}
