package graph;

import java.util.ArrayList;


public class PropertyFinder {

    static boolean tempCycle = false;

    public static int maxDegree(Graph g) {
        ArrayList<Vertex> vertices;
        int max, numEdges, numVertices;

        vertices = g.getVertices();
        numVertices = g.vertexCount();
        max = 0;

        for (int i = 0; i < numVertices; i++) {
            numEdges = vertices.get(i).numberConnections();
            if (numEdges > max) {
                max = numEdges;
            }
        }

        return max;
    }

    public static boolean isBipartite() {
        return Vertex.getMaxColor() <= 2;
    }

    public static boolean isConnected(Graph g) {
        if (g.vertexCount() == 0) {
            return true;
        }

        ArrayList<Vertex> visited = new ArrayList<Vertex>();
        Vertex v;
        v = g.getVertex(0);
        connectedH(visited, v);

        return visited.size() == g.vertexCount();
    }

    public static void connectedH(ArrayList<Vertex> visited, Vertex v) {
        if (visited.contains(v)) {
            return;
        }

        visited.add(v);
        for (Vertex temp : v.getConnections()) {
            connectedH(visited, temp);
        }

    }


    public static boolean isTree(Graph g) {
        tempCycle = false;
        if (g.connectionCount() / 2 != g.vertexCount() - 1) {
            return false;
        }
        for (Vertex v : g.getVertices()) {
            ArrayList<Vertex> visited = new ArrayList<Vertex>();
            cycleExists(v, v, null, visited);
            if (tempCycle) {
                return false;
            }
        }
        return true;
    }

    private static void cycleExists(Vertex original, Vertex parent, Vertex v,
            ArrayList<Vertex> visited) {
        if (tempCycle) {
            return;
        }
        if (v == null) {
            for (Vertex temp : original.getConnections()) {
                cycleExists(original, original, temp, visited);
            }
        } else if (v.equals(original)) {
            tempCycle = true;
            return;
        } else if (visited.contains(v)) {
            return;
        } else {
            visited.add(v);
            for (Vertex temp : v.getConnections()) {
                if (!temp.equals(parent)) {
                    cycleExists(original, v, temp, visited);
                }
            }
        }
    }
}
