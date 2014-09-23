package visualization;

import java.awt.*;

public class InfoPanel {
    private int x, y;
    private int width, height;

    public InfoPanel() {
        width = 100;
        height = 100;
    }

    public void setPosition(int x, int y) {
        this.x = x - width;
        this.y = y - height;
    }

    public void draw(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        if (Drawer.infoNode == null) {
            return;
        }
        g.setColor(new Color(245, 241, 169));
        g.fillRect(x, y, width, height);
        g.setColor(Color.black);
        g.drawString("Vertex ID: " + Drawer.infoNode.getID(), x + 2, y + 12);
        g.drawString("Color: " + Drawer.infoNode.getColor(), x + 2, y + 27);
        g.drawString("Degree: " + Drawer.infoNode.getDegree(), x + 2, y + 42);
        g.drawString("Circuits: " + Drawer.graph.findCircuits(Drawer.infoNode).size(), x + 2, y + 57);
    }
}