package visualization;

import graph.Edge;
import util.FileOperations;
import graph.Graph;
import graph.PropertyFinder;
import graph.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Drawer extends JPanel implements MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 5174812665272092921L;
    public static JFrame frame;
    public static Graph graph = new Graph();
    public static Vertex selectedVertex = null;
    static Vertex infoNode = null;
    static InfoPanel info = new InfoPanel();

    static int oldWidth, oldHeight;

    static long timer = System.currentTimeMillis() - 1000;

    static long dragTimer = -1;

    static int canvasX, canvasY;

    static int mouseX, mouseY;

    static boolean draggingCanvas = false;
    static int originalCanvasX, originalCanvasY;
    static int originalMouseX, originalMouseY;

    public static short mode = 0;
    static ArrayList<visualization.Button> buttons = new ArrayList<visualization.Button>();

    static ArrayList<Edge> lines = new ArrayList<Edge>();

    static boolean dragged = false;

    static Font drawFont = null;
    static boolean initFont = false;

    static boolean isConnected = true;
    static boolean isTree = true;
    public static short oldMode = 0;

    public Drawer() {
        frame = new JFrame("Nodes");
        frame.setVisible(true);
        oldWidth = 1024;
        oldHeight = 720;
        frame.setSize(oldWidth, oldHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
        frame.add(this);
    }

    public static void main(String args[]) {
        new Drawer();
        canvasX = canvasY = 0;
        initButtons();

        drawFont = new Font("Arial", Font.BOLD, 16);
    }

    public static void buttonAction() {
        if (mode == 3) {
            try {
                FileOperations.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode == 4) {
            try {
                FileOperations.loadFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initButtons() {
        buttons.clear();
        int x = frame.getInsets().left;
        int y = frame.getInsets().top;
        visualization.Button vertexButton = new visualization.Button((short) 0, frame.getWidth() - x - 200,
                frame.getHeight() - y - 50, 100, 50);
        vertexButton.setText("Vertex");
        visualization.Button connectionButton = new visualization.Button((short) 1, frame.getWidth() - x - 100,
                frame.getHeight() - y - 50, 100, 50);
        connectionButton.setText("Connection");
        visualization.Button removeButton = new visualization.Button((short) 2, frame.getWidth() - x - 300,
                frame.getHeight() - y - 50, 100, 50);
        removeButton.setText("Remove");
        buttons.add(vertexButton);
        buttons.add(connectionButton);
        buttons.add(removeButton);

        visualization.Button saveButton = new visualization.Button((short) 3, 0, frame.getHeight() - y - 50, 100, 50);
        saveButton.setText("Save");
        visualization.Button loadButton = new visualization.Button((short) 4, 100, frame.getHeight() - y - 50, 100, 50);
        loadButton.setText("Load");
        buttons.add(saveButton);
        buttons.add(loadButton);
    }

    public void paintComponent(Graphics g) {
        g.setFont(drawFont);
        //frame.setTitle(dragged+" "+dragTimer);
        if (System.currentTimeMillis() - timer >= 32) {
            timer = System.currentTimeMillis();

            if (frame.getWidth() != oldWidth || frame.getHeight() != oldHeight) {
                oldWidth = frame.getWidth();
                oldHeight = frame.getHeight();
                initButtons();
            }
            g.setColor(frame.getBackground());
            g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

            g.setColor(Color.white);


            if (mode == 1 && selectedVertex != null) {
                g.drawLine(selectedVertex.getX() + canvasX, selectedVertex.getY() + canvasY,
                        mouseX, mouseY);
            }

            for (Vertex v : graph.getVertices()) {
                if (!v.isSelected()) {
                    v.drawConnections(g, canvasX, canvasY);
                }
            }
            if (selectedVertex != null) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2.5f));
                selectedVertex.drawConnections(g, canvasX, canvasY);
                g2.setStroke(new BasicStroke(1f));
            }
            for (Vertex v : graph.getVertices()) {
                v.draw(g, canvasX, canvasY);
            }

            for (visualization.Button b : buttons) {
                b.draw(g);
            }

            if (graph != null && graph.getVertices() != null) {
                g.setColor(frame.getBackground());
                g.fillRect(0, 0, 200, 150);
                g.setColor(Color.white);
                g.drawString("Number of Vertices: " + graph.vertexCount(), 10, 20);
                g.drawString("Number of Connections: " + graph.connectionCount() / 2, 10, 40);
                g.drawString("Number of Colors: " + Vertex.getMaxColor(), 10, 60);
                g.drawString("Maximum Degree: " + PropertyFinder.maxDegree(graph), 10, 80);
                g.drawString("Bipartite: " + PropertyFinder.isBipartite(), 10, 100);
                g.drawString("Connected: " + isConnected, 10, 120);
                g.drawString("Tree: " + isTree, 10, 140);
            }

            info.draw(g);
        }

        repaint();
    }

    private static void resetColor() {
        Vertex.resetMaxColor();
        for (Vertex v : graph.getVertices()) {
            v.resetColor();
        }
    }

    private static void checkConnected() {
        isConnected = PropertyFinder.isConnected(graph);
        isTree = PropertyFinder.isTree(graph);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        mouseX = x;
        mouseY = y;

        if (draggingCanvas) {
            infoNode = null;
            canvasX = originalCanvasX - (originalMouseX - x);
            canvasY = originalCanvasY - (originalMouseY - y);
        }

        if (!dragged) {
            if (dragTimer == -1) {
                dragTimer = 3;
            } else {
                dragTimer--;
                if (dragTimer == 0) {
                    dragged = true;
                }
            }
        }

        if (mode == 0) {
            if (selectedVertex != null) {
                info.setPosition(x, y);
                selectedVertex.setPosition(x - canvasX, y - canvasY);
            }
        }
    }

    public static void initLines() {
        lines.clear();
        for (Vertex v : graph.getVertices()) {
            for (Vertex vv : v.getConnections()) {
                Edge temp = new Edge(v, vv);
                if (!lines.contains(temp)) {
                    lines.add(temp);
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        for (int i = 0; i < graph.vertexCount(); i++) {
            Vertex v = graph.getVertex(i);
            double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                    2) + Math.pow(y - canvasY - v.getY(), 2));
            if (distance < Vertex.getRadius()) {
                infoNode = v;
                info.setPosition(x, y);
                return;
            }
        }
        infoNode = null;
    }

    public void mouseClicked(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        mouseX = x;
        mouseY = y;

        if (e.getButton() == MouseEvent.BUTTON1) {

            for (visualization.Button b : buttons) {
                if (b.isWithin(x, y)) {
                    mode = b.getMode();
                    return;
                }
            }

            if (mode == 0)//VertexMode
            {
                for (int i = 0; i < graph.vertexCount(); i++) {
                    Vertex v = graph.getVertex(i);
                    double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                            2) + Math.pow(y - canvasY - v.getY(), 2));
                    if (distance < Vertex.getRadius()) {
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.unselect();
                            selectedVertex = null;
                        }
                        selectedVertex = v;
                        v.select();
                        return;
                    }
                    if (v.isSelected()) {
                        v.unselect();
                    }
                }
                Vertex temp = new Vertex(x - canvasX, y - canvasY);
                temp.initialize();
                graph.addVertex(temp);
                checkConnected();
            } else if (mode == 1)//Connection mode
            {
                for (int i = 0; i < graph.vertexCount(); i++) {
                    Vertex v = graph.getVertex(i);
                    double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                            2) + Math.pow(y - canvasY - v.getY(), 2));
                    if (distance < Vertex.getRadius()) {
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.unselect();
                            selectedVertex = null;
                        }
                        selectedVertex = v;
                        v.select();
                        return;
                    }
                    if (v.isSelected()) {
                        v.unselect();
                    }
                }
                if (selectedVertex != null)//There already is a selected node
                {
                    selectedVertex.unselect();
                    selectedVertex = null;
                }
            } else if (mode == 2)//Remove mode
            {
                for (int i = 0; i < graph.vertexCount(); i++) {
                    Vertex v = graph.getVertex(i);
                    double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                            2) + Math.pow(y - canvasY - v.getY(), 2));
                    if (distance < Vertex.getRadius()) {
                        for (Vertex temp : v.getConnections()) {
                            temp.removeConnection(v);
                        }
                        graph.removeVertex(v);
                        resetColor();
                        setColor();
                        return;
                    }
                }

                Edge closestEdge = null;
                double closestDistance = Double.MAX_VALUE;
                for (int i = 0; i < lines.size(); i++) {
                    Edge temp = lines.get(i);
                    double dist = temp.distance(x - canvasX, y - canvasY);
                    if (dist < closestDistance) {
                        closestDistance = dist;
                        closestEdge = temp;
                    }
                }
                if (closestDistance < 5.0) {
                    closestEdge.removeConnection();
                    resetColor();
                    setColor();
                    checkConnected();
                }
            }


        } else if (e.getButton() == MouseEvent.BUTTON3) {
            originalMouseX = x;
            originalMouseY = y;
            originalCanvasX = canvasX;
            originalCanvasY = canvasY;
            draggingCanvas = true;
        }
    }

    private static void setColor() {
        if (graph.vertexCount() == 0) {
            return;
        }
        graph.getVertex(0).initialize();
        for (int i = 1; i < graph.vertexCount(); i++) {
            Vertex v = graph.getVertex(i);
            v.chooseColor();
        }
        Vertex.initColors();
        checkConnected();
    }

    public void mouseReleased(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        mouseX = x;
        mouseY = y;

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (dragged) {
                if (mode == 0) {
                    if (selectedVertex != null) {
                        selectedVertex.unselect();
                    }
                    selectedVertex = null;
                } else if (mode == 1) {
                    if (selectedVertex != null) {
                        for (int i = 0; i < graph.vertexCount(); i++) {
                            Vertex v = graph.getVertex(i);
                            if (v.equals(selectedVertex)) {
                                continue;
                            }
                            double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                                    2) + Math.pow(y - canvasY - v.getY(), 2));
                            if (distance < Vertex.getRadius()) {
                                if (v.isNeighbor(selectedVertex)) {
                                    v.removeConnection(selectedVertex);
                                    selectedVertex.removeConnection(v);
                                    resetColor();
                                    selectedVertex.unselect();
                                    selectedVertex = null;
                                    setColor();
                                    return;
                                } else {
                                    resetColor();
                                    lines.add(new Edge(v, selectedVertex));
                                    selectedVertex.addConnection(v, true);
                                    selectedVertex.unselect();
                                    info.setPosition(x, y);
                                    infoNode = v;
                                    selectedVertex = null;
                                    setColor();
                                    return;
                                }
                            }
                        }
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.unselect();
                            selectedVertex = null;
                        }
                    }
                }
            }
            dragged = false;
            dragTimer = -1;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            draggingCanvas = false;
        }
    }

    public static void reset() {
        graph = new Graph();
        infoNode = null;
        selectedVertex = null;
        isTree = true;
        isConnected = true;
    }

    public static void initialize() {
        resetColor();
        setColor();
        checkConnected();
    }
}