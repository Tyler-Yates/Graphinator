package visualization;

import graph.Vertex;

import java.awt.*;

/**
 * Used to display various information about elements on screen.
 */
public class InfoPanel {
    private int x, y;
    private int width = 100;
    private int height = 100;

    private static final Color backgroundColor = new Color(245, 241, 169);

    /**
     * Sets the position of the info panel to the given position on screen.
     *
     * @param position the position
     */
    public void setPosition(ScreenPosition position) {
        x = position.getX() - width;
        y = position.getY() - height;
    }

    /**
     * Draws the current info panel to the given canvas.
     *
     * @param g the canvas
     */
    public void draw(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        // Don't draw if we don't have a node to get information from
        final Vertex infoNode = Drawer.getInfoNode();
        if (infoNode == null) {
            return;
        }

        g.setColor(backgroundColor);
        g.fillRect(x, y, width, height);
        g.setColor(Color.black);
        g.drawString("Vertex ID: " + infoNode.getID(), x + 2, y + 12);
        g.drawString("Color: " + infoNode.getColor(), x + 2, y + 27);
        g.drawString("Degree: " + infoNode.getDegree(), x + 2, y + 42);
    }
}