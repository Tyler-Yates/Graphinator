package graph;

import java.awt.geom.Line2D;

public class Edge implements Comparable<Edge> {
    private Line2D line;
    private Vertex vertex1, vertex2;

    public Edge(Vertex start, Vertex end) {
        vertex1 = start;
        vertex2 = end;

        line = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public double distance(int mx, int my) {
        return line.ptSegDist(mx, my);
    }

    public void removeConnection() {
        if (vertex1 != null) {
            vertex1.removeConnection(vertex2);
        }
        if (vertex2 != null) {
            vertex2.removeConnection(vertex1);
        }
    }

    public boolean equals(Edge e) {
        return (e.vertex1 == vertex1 && e.vertex2 == vertex2) || (e.vertex1 == vertex2 && e
                .vertex2 == vertex1);
    }

    public String toString() {
        return vertex1 + " " + vertex2;
    }

    public int compareTo(Edge e) {
        boolean bool = (e.vertex1 == vertex1 && e.vertex2 == vertex2) || (e.vertex1 == vertex2 &&
                e.vertex2 == vertex1);
        if (bool) {
            return 0;
        } else {
            return 1;
        }
    }
}