package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class PropertyFinder {

    private final Graph graph;

    static boolean tempCycle = false;

    /**
     * Constructs a property finder for the given graph.
     *
     * @param graph the given graph
     */
    public PropertyFinder(Graph graph) {
        this.graph = graph;
    }

    public int maxDegree(Graph g) {
        Collection<Vertex> vertices;
        int max, numEdges, numVertices;

        vertices = g.getVertices();
        numVertices = g.numberOfVertices();
        max = 0;

        for (Vertex vertex : vertices) {
            numEdges = vertex.getDegree();
            if (numEdges > max) {
                max = numEdges;
            }
        }

        return max;
    }

    public boolean isBipartite() {
        return graph.getColorManager().numberOfColors() <= 2;
    }

    public boolean isConnected(Graph g) {
        if (g.numberOfVertices() == 0) {
            return true;
        }

        final Set<Vertex> visited = new HashSet<>();
        Vertex v = g.getVertex(0);
        connectedH(visited, v);

        return visited.size() == g.numberOfVertices();
    }

    public void connectedH(Set<Vertex> visited, Vertex v) {
        if (visited.contains(v)) {
            return;
        }

        visited.add(v);
        for (Vertex temp : v.getNeighbors()) {
            connectedH(visited, temp);
        }

    }


    public boolean isTree(Graph g) {
        tempCycle = false;
        if (g.numberOfConnections() / 2 != g.numberOfVertices() - 1) {
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

    private void cycleExists(Vertex original, Vertex parent, Vertex v, ArrayList<Vertex> visited) {
        if (tempCycle) {
            return;
        }
        if (v == null) {
            for (Vertex temp : original.getNeighbors()) {
                cycleExists(original, original, temp, visited);
            }
        } else if (v.equals(original)) {
            tempCycle = true;
            return;
        } else if (visited.contains(v)) {
            return;
        } else {
            visited.add(v);
            for (Vertex temp : v.getNeighbors()) {
                if (!temp.equals(parent)) {
                    cycleExists(original, v, temp, visited);
                }
            }
        }
    }

    public ArrayList<Circuit> findCircuits(Vertex v) {
        if (v == null) {
            return null;
        }
        ArrayList<Circuit> circuits = new ArrayList<Circuit>();


        for (Vertex temp : v.getNeighbors()) {
            ArrayList<Connection> connections = new ArrayList<>();
            connections.add(new Connection(v, temp));
            Circuit c = new Circuit();
            c.addVertex(v);
            findCircuitsHelper(temp, v, c, connections, circuits);
        }
        return circuits;
    }

    private void findCircuitsHelper(Vertex current, Vertex original, Circuit currentPath,
            ArrayList<Connection> connections, ArrayList<Circuit> ans) {
        //System.out.println(current+" "+connections);
        currentPath.addVertex(current);
        outer:
        for (Vertex v : current.getNeighbors()) {
            Connection newConnect = new Connection(current, v);
            for (Connection e : connections) {
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
    }
}
