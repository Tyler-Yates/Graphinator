package visualization;

import util.MouseMode;

import javax.swing.JFrame;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the cursor image.
 */
public class CursorManager {

    private static final Map<MouseMode, String> cursorImages = new HashMap<>();

    static {
        final String cursorImageFolder = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "cursor" + File.separator;

        cursorImages.put(MouseMode.VERTEX, cursorImageFolder + "vertex.png");
        cursorImages.put(MouseMode.CONNECTION, cursorImageFolder + "connection.png");
        cursorImages.put(MouseMode.REMOVE, cursorImageFolder + "remove.png");
    }

    /**
     * Sets the image of the cursor to the one corresponding to the given {@link MouseMode}.
     *
     * @param frame the frame containing the mouse
     * @param mouseMode the mouse mode
     */
    public static void setCursor(JFrame frame, MouseMode mouseMode) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final String filePath = cursorImages.get(mouseMode);

        // Only set the cursor to the custom image if it exists
        if (filePath != null && new File(filePath).exists()) {
            final Image image = toolkit.getImage(filePath);
            final Cursor c = toolkit.createCustomCursor(image, new Point(0, 0), "img");
            frame.setCursor(c);
        } else {
            // Otherwise just set it to the default cursor
            frame.setCursor(Cursor.getDefaultCursor());
        }
    }
}
