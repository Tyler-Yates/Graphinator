import java.util.HashSet;


public class Circuit {
    private HashSet<Vertex> path = new HashSet<Vertex>();

    public void Circuit() {
        //path = new HashSet<Vertex>();
    }

    public void Circuit(Vertex... vertexs) {
        path = new HashSet<Vertex>();
        for (int i = 0; i < vertexs.length; i++) {
            path.add(vertexs[i]);
        }
    }

    public void addVertex(Vertex v) {
        path.add(v);
    }

    public Circuit clone() {
        Circuit newCircuit = new Circuit();
        newCircuit.path = (HashSet<Vertex>) path.clone();
        return newCircuit;
    }

    public void removeVertex(Vertex v) {
        path.remove(v);
    }

    public HashSet<Vertex> getPath() {
        return path;
    }
}