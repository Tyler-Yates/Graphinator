package graph;

import java.util.Iterator;

/**
 * Used to detect and report the properties of a graph.
 */
public class PropertyManager {

    private final Graph graph;

    private int maxDegree = 0;
    private boolean bipartite = false;
    private boolean tree = false;
    private boolean connected = true;
    private boolean regular;

    //static boolean tempCycle = false;

    /**
     * Constructs a property manager for the given graph.
     *
     * @param graph the given graph
     */
    public PropertyManager(Graph graph) {
        this.graph = graph;
    }

    void graphIsConnected() {
        connected = true;
    }

    void graphIsDisconnected() {
        connected = false;
    }

    private void calculateMaxDegree() {
        int max = 0;
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.getDegree() > max) {
                max = vertex.getDegree();
            }
        }

        maxDegree = max;
    }

    /**
     * Returns the maximum degree of any of the vertices of the graph.
     *
     * @return the maximum degree
     */
    public int getMaxDegree() {
        return maxDegree;
    }

    private void calculateBipartite() {
        bipartite = graph.numberOfColors() <= 2;
    }

    /**
     * Returns whether the graph is bipartite.
     *
     * @return whether the graph is bipartite
     */
    public boolean isBipartite() {
        return bipartite;
    }

    /**
     * Returns whether the graph is connected.
     *
     * @return whether the graph is connected
     */
    public boolean isConnected() {
        return connected;
    }

    private void calculateTree() {
        // TODO fix definition for directed graphs
        tree = isConnected() && graph.numberOfConnections() / 2 == graph.numberOfVertices() - 1;
    }

    /**
     * Returns whether the graph is a tree.
     *
     * @return whether the graph is a tree
     */
    public boolean isTree() {
        return tree;
    }

    private void calculateRegularity() {
        if(graph.numberOfVertices() > 0) {
            final Iterator<Vertex> iterator = graph.getVertices().iterator();
            final int degree = iterator.next().getDegree();
            while (iterator.hasNext()) {
                if (iterator.next().getDegree() != degree) {
                    regular = false;
                    return;
                }
            }
        }
        regular = true;
    }

    /**
     * Returns whether the graph is regular.
     *
     * @return whether the graph is regular
     */
    public boolean isRegular() {
        return regular;
    }

    /**
     * Returns whether the graph is complete.
     *
     * @return whether the graph is complete
     */
    public boolean isComplete() {
        return maxDegree == graph.numberOfVertices() - 1 && isRegular();
    }

    /*private void cycleExists(Vertex original, Vertex parent, Vertex v, ArrayList<Vertex>
    visited) {
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
    }*/

    void calculateProperties() {
        calculateMaxDegree();
        calculateTree();
        calculateBipartite();
        calculateRegularity();
    }
}
