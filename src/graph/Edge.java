package graph;

import java.awt.*;
import java.awt.geom.Line2D;

public class Edge implements Comparable<Edge> {
    private Vertex start, end;
    private boolean selected;

    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
    }

    public double distance(int mx, int my) {
        final Line2D line = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
        return line.ptSegDist(mx, my);
    }

    public void removeConnection() {
        if (start != null) {
            start.removeConnection(end);
        }
        if (end != null) {
            end.removeConnection(start);
        }
    }

    public boolean equals(Edge e) {
        return (e.start == start && e.end == end) || (e.start == end && e.end == start);
    }

    public String toString() {
        return start + " " + end;
    }

    public int compareTo(Edge e) {
        boolean bool = (e.start == start && e.end == end) || (e.start == end && e.end == start);
        if (bool) {
            return 0;
        } else {
            return 1;
        }
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public void paint(Graphics g) {
        if (selected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }

        g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public Vertex getEnd() {
        return end;
    }

    public Vertex getStart() {
        return start;
    }
}