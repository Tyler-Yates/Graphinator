package graph;

import java.util.ArrayList;


public class Graph {
    protected ArrayList<Vertex> vertices = new ArrayList<Vertex>();

    public Graph(ArrayList<Vertex> readObject) {
        vertices = readObject;
    }

    public Graph() {

    }

    public Graph(Vertex... vertexs) {
        vertices = new ArrayList<Vertex>();
        for (int i = 0; i < vertexs.length; i++) {
            vertices.add(vertexs[i]);
        }
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(int index) {
        if (index >= 0 && index < vertices.size()) {
            return vertices.get(index);
        }
        return null;
    }

    public Vertex getVertexByID(int id) {
        for (Vertex vertex : vertices) {
            if (vertex.getID() == id) {
                return vertex;
            }
        }
        return null;
    }

    public boolean addVertex(Vertex a) {
        vertices.add(a);
        return true;
    }

    public void removeVertex(Vertex b) {
        vertices.remove(b);
    }

    public int connectionCount() {
        int sum = 0;
        for (Vertex v : vertices) {
            sum += v.numberConnections();
        }
        return sum;
    }

    public int vertexCount() {
        return vertices.size();
    }

    public ArrayList<Circuit> findCircuits(Vertex v) {
        if (v == null) {
            return null;
        }
        ArrayList<Circuit> circuits = new ArrayList<Circuit>();


        for (Vertex temp : v.getConnections()) {
            ArrayList<Edge> connections = new ArrayList<Edge>();
            connections.add(new Edge(v, temp));
            Circuit c = new Circuit();
            c.addVertex(v);
            findCircuitsHelper(temp, v, c, connections, circuits);
        }
        return circuits;
    }

    private void findCircuitsHelper(Vertex current, Vertex original, Circuit currentPath,
            ArrayList<Edge> connections, ArrayList<Circuit> ans) {
        //System.out.println(current+" "+connections);
        currentPath.addVertex(current);
        try {
            //Thread.sleep(1000);
        } catch (Exception e) {
        }
        outer:
        for (Vertex v : current.getConnections()) {
            Edge newConnect = new Edge(current, v);
            for (Edge e : connections) {
                if (e.equals(newConnect)) {
                    continue outer;
                }
            }
            if (v.equals(original)) {
                ans.add(currentPath.clone());
            } else {
                connections.add(newConnect);
                currentPath.addVertex(v);
                findCircuitsHelper(v, original, currentPath, connections, ans);
            }
        }
        return;
    }
}