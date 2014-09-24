package graph;

import visualization.Drawer;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Vertex implements Comparable<Vertex>, Serializable {
    private static final long serialVersionUID = 1122311460388446576L;
    private int color = 0;
    private int drawX, drawY;
    private int id;
    private static final int radius = 20;
    private static int ID = 0;
    private static int maxColor = 1;
    private static ArrayList<Color> colors = null;
    private HashSet<Vertex> connections = new HashSet<Vertex>();
    private ArrayList<Integer> invalidColors = new ArrayList<Integer>();

    private boolean selected = false;


    public Vertex(int dx, int dy) {
        id = ID++;
        drawX = dx;
        drawY = dy;
    }

    public Vertex(int id, int dx, int dy) {
        this.id = id;
        ID = id;
        drawX = dx;
        drawY = dy;
    }

    public void select() {
        selected = true;
    }

    public void unselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setPosition(int dx, int dy) {
        drawX = dx;
        drawY = dy;
    }

    public int getID() {
        return id;
    }

    public static int getRadius() {
        return radius;
    }

    public boolean equals(Vertex other) {
        return getID() == other.getID();
    }

    public void addConnection(Vertex other, boolean initial) {
        if (other == null) {
            return;
        }
        connections.add(other);
        if (initial) {
            other.addConnection(this, false);
        }
    }

    public void initialize() {
        if (color != 0) {
            return;
        }

        color = 1;

        for (Vertex v : connections) {
            v.chooseColor();
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int colorValue) {
        if (colorValue > maxColor) {
            maxColor = colorValue;
        }
        color = colorValue;
    }

    public void chooseColor() {
        if (color != 0) {
            return;
        }

        for (Vertex v : connections) {
            int neighborColor = v.getColor();
            invalidColors.add(neighborColor);
        }

        int k = 1;
        while (color == 0) {
            if (!invalidColors.contains(k)) {
                setColor(k);
            }
            k++;
        }

        for (Vertex v : connections) {
            v.chooseColor();
        }
    }

    public HashSet<Vertex> getConnections() {
        return connections;
    }

    public static void initColors() {
        Random generator = new Random();
        double golden_ratio_conjugate = 0.618033988749895;
        double HSB;
        int toRGB;
        if (colors == null) {
            colors = new ArrayList<Color>();
        } else {
            return;
        }
        colors.add(null);
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.red);
        colors.add(Color.blue);
        colors.add(Color.GREEN);
        colors.add(Color.yellow);
        colors.add(Color.MAGENTA);
        colors.add(Color.orange);

        for (int i = 0; i < 1000; i++) {
            HSB = generator.nextDouble();
            HSB += golden_ratio_conjugate;
            HSB %= 1;
            toRGB = Color.HSBtoRGB((float) HSB, (float) 0.99, (float) 0.99);
            colors.add(new Color(toRGB));
        }
    }

    private static Color getColor(int value) {
        if (colors == null) {
            initColors();
        }
        if (value >= colors.size()) {
            return null;
        }
        return colors.get(value);
    }

    public int getX() {
        return drawX;
    }

    public int getY() {
        return drawY;
    }

    public void draw(Graphics g, int cX, int cY) {
        Color c = getColor(color);
        if (c == null) {
            c = Drawer.frame.getBackground();
        }
        g.setColor(c);
        g.fillOval(drawX - radius / 2 + cX, drawY - radius / 2 + cY, radius, radius);

        if (selected) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.white);
        }
        g.drawOval(drawX - radius / 2 + cX, drawY - radius / 2 + cY, radius, radius);
    }

    public void drawConnections(Graphics g, int cX, int cY) {
        if (selected) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.white);
        }
        for (Vertex v : connections) {
            if (Drawer.selectedVertex == null || !Drawer.selectedVertex.equals(v)) {
                g.drawLine(drawX + cX, drawY + cY, v.getX() + cX, v.getY() + cY);
            }
        }
    }

    public int numberConnections() {
        return connections.size();
    }

    public static int getMaxColor() {
        return maxColor;
    }

    public String toString() {
        return "" + getID();
    }

    public void resetColor() {
        invalidColors.clear();
        color = 0;
    }

    public void removeConnection(Vertex other) {
        connections.remove(other);
    }

    public boolean isNeighbor(Vertex other) {
        return connections.contains(other);
    }

    @Override
    public int compareTo(Vertex o) {
        return o.getID() - ID;
    }

    public static void resetMaxColor() {
        maxColor = 1;
    }

    public int getDegree() {
        return connections.size();
    }
}