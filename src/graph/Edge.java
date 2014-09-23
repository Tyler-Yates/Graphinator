package graph;

public class Edge implements Comparable<Edge> {
    private int sx, sy, tx, ty;
    private Vertex sv, tv;

    public Edge(Vertex start, Vertex end) {
        sx = start.getX();
        sy = start.getY();
        tx = end.getX();
        ty = end.getY();
        sv = start;
        tv = end;
    }

    public double distance(int mx, int my) {
        double hypotenuse = Math.sqrt(Math.pow(tx - sx, 2) + Math.pow(ty - sy, 2));
        return Math.abs((mx - sx) * (ty - sy) - (my - sy) * (tx - sx)) / hypotenuse;
    }

    public void removeConnection() {
        if (sv != null) {
            sv.removeConnection(tv);
        }
        if (tv != null) {
            tv.removeConnection(sv);
        }
    }

    public boolean equals(Edge e) {
        return (e.sv == sv && e.tv == tv) || (e.sv == tv && e.tv == sv);
    }

    public String toString() {
        return "" + sv + " " + tv;
    }

    public int compareTo(Edge e) {
        boolean bool = (e.sv == sv && e.tv == tv) || (e.sv == tv && e.tv == sv);
        if (bool) {
            return 0;
        } else {
            return 1;
        }
    }
}