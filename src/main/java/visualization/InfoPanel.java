package visualization;

import graph.Vertex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Used to display various information about elements on screen.
 */
public class InfoPanel {
    private Vertex vertex;
    private int width = 100;
    private int height = 100;
    private Diagonal drawDirection = Diagonal.UPPER_LEFT;

    private static final Color backgroundColor = new Color(245, 241, 169);

    /**
     * Sets the vertex that the info panel will display information about.
     *
     * @param vertex the vertex
     */
    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    /**
     * Sets the draw direction for the info panel.
     *
     * @param drawDirection the draw direction
     */
    public void setDrawDirection(Diagonal drawDirection) {
        this.drawDirection = drawDirection;
    }

    /**
     * Draws the current info panel to the given canvas.
     *
     * @param g the canvas
     * @param canvasX the x canvas shift
     * @param canvasY the y canvas shift
     */
    public void draw(Graphics g, int canvasX, int canvasY) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        // Don't draw if we don't have a node to get information from
        final Vertex infoNode = Drawer.getInfoNode();
        if (infoNode == null) {
            return;
        }

        g.setColor(backgroundColor);

        final RectangleOnScreen rectangle = getRectangle(drawDirection, canvasX, canvasY);

        final int upperLeftX = rectangle.getX();
        final int upperLeftY = rectangle.getY();
        g.fillRect(upperLeftX, upperLeftY, width, height);
        g.setColor(Color.black);
        g.drawString("Vertex ID: " + infoNode.getID(), upperLeftX + 2, upperLeftY + 12);
        g.drawString("Color: " + infoNode.getColor(), upperLeftX + 2, upperLeftY + 27);
        g.drawString("Degree: " + infoNode.getDegree(), upperLeftX + 2, upperLeftY + 42);
    }

    /**
     * Returns the rectangle that the info panel would draw on-screen if it used the given draw
     * direction.
     *
     * @param drawDirection the given draw direction
     * @param canvasX the x canvas shift
     * @param canvasY the y canvas shift
     *
     * @return the rectangle
     */
    public RectangleOnScreen getRectangle(Diagonal drawDirection, int canvasX, int canvasY) {
        final ScreenPosition vertexScreenPosition = vertex.getScreenPosition(canvasX, canvasY);
        final int x = vertexScreenPosition.getX();
        final int y = vertexScreenPosition.getY();

        int upperLeftX;
        int upperLeftY;
        switch (drawDirection) {
            case UPPER_LEFT:
                upperLeftX = x - width;
                upperLeftY = y - height;
                break;
            case UPPER_RIGHT:
                upperLeftX = x;
                upperLeftY = y - height;
                break;
            case LOWER_LEFT:
                upperLeftX = x - width;
                upperLeftY = y;
                break;
            case LOWER_RIGHT:
                upperLeftX = x;
                upperLeftY = y;
                break;
            default:
                upperLeftX = x;
                upperLeftY = y;
        }

        upperLeftX += Diagonal.getX(drawDirection, vertex.getRadius());
        upperLeftY -= Diagonal.getY(drawDirection, vertex.getRadius());

        return new RectangleOnScreen(new ScreenPosition(upperLeftX, upperLeftY), width, height);
    }
}