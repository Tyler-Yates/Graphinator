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
    private HashSet<Edge> connections = new HashSet<Edge>();
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

    public void addConnection(Edge connection) {
        if (connection == null) {
            return;
        }
        connections.add(connection);
    }

    public void initialize() {
        if (color != 0) {
            return;
        }

        color = 1;

        for (Vertex v : getNeighbors()) {
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

        for (Vertex v : getNeighbors()) {
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

        for (Vertex v : getNeighbors()) {
            v.chooseColor();
        }
    }

    public HashSet<Edge> getConnections() {
        return connections;
    }

    public HashSet<Vertex> getNeighbors() {
        final HashSet<Vertex> vertexes = new HashSet<Vertex>();
        for (Edge edge : connections) {
            vertexes.add(edge.getEnd());
        }

        return vertexes;
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
        final Edge removal = new Edge(this, other);
        connections.remove(removal);
    }

    public void removeConnection(Edge connection) {
        connections.remove(connection);
    }

    public boolean isNeighbor(Vertex other) {
        final Edge check = new Edge(this, other);
        return connections.contains(check);
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