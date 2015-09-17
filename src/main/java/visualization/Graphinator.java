package visualization;

/**
 * Container class for the program.
 */
public class Graphinator {
    private static Drawer drawer;

    /**
     * Starts the graphinator program.
     *
     * @param args the program arguments
     */
    public static void main(String args[]) {
        drawer = new Drawer();
    }

    /**
     * Initiates an immediate redraw of the program.
     */
    public static void redraw() {
        drawer.repaint();
    }
}
