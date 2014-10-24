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
     * @param position the position of the button on the screen
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param text the text of the button
     * @param action the action the button will perform
     */
    public ActionButton(ScreenPosition position, int width, int height, String text,
            Action action) {
        super(position, width, height, text);
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
